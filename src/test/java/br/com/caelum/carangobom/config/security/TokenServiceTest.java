package br.com.caelum.carangobom.config.security;

import br.com.caelum.carangobom.marca.controller.MarcaController;
import br.com.caelum.carangobom.usuario.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    private TokenService tokenService;

    @Mock
    Authentication authentication;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "qualquerSegredo");
        ReflectionTestUtils.setField(tokenService, "expiracao", "1800000");
    }

    @Test
    public void deveGerarTokenValido() {
        Usuario usuario = new Usuario(1L, "teste", "123456");
        when(authentication.getPrincipal()).thenReturn(usuario);
        String token = tokenService.gerarToken(authentication);
        Jws<Claims> claimsJws = tokenService.getClaimsJws(token);
        assertEquals("API da Carango Bom", claimsJws.getBody().getIssuer());
        assertEquals(usuario.getId(), tokenService.getIdUsuario(token));
        assertTrue(tokenService.isTokenValido(token));
    }

    @Test
    public void deveVerificarTokenInvalido(){
        String tokenInvalido = "Bearer 2222222";
        assertFalse(tokenService.isTokenValido(tokenInvalido));
    }


}
package br.com.caelum.carangobom.autenticacao.controller;

import br.com.caelum.carangobom.autenticacao.controller.dto.TokenDto;
import br.com.caelum.carangobom.autenticacao.controller.form.LoginForm;
import br.com.caelum.carangobom.config.security.TokenService;
import br.com.caelum.carangobom.usuario.controller.dto.UsuarioDto;
import br.com.caelum.carangobom.usuario.model.Usuario;
import br.com.caelum.carangobom.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AutenticacaoControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AutenticacaoController autenticacaoController;
    public LoginForm loginForm;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);
        loginForm = new LoginForm();
        loginForm.setNome("usuario");
        loginForm.setSenha("123456");
    }

    @Test
    public void deveAutenticarUsuarioERetornarTokenJWT() {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginForm.getNome(), loginForm.getSenha());
        when(authenticationManager.authenticate(usernamePasswordAuthenticationToken)).thenReturn(usernamePasswordAuthenticationToken);
        when(tokenService.gerarToken(usernamePasswordAuthenticationToken)).thenReturn("NovoToken");

        ResponseEntity<TokenDto> autenticar = autenticacaoController.autenticar(loginForm);

        assertEquals(HttpStatus.OK, autenticar.getStatusCode());
        assertEquals(new TokenDto("NovoToken", "Bearer"), autenticar.getBody());
    }

    @Test
    public void deveAutenticarUsuarioInvalidoERetornarBadRequest() {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginForm.getNome(), loginForm.getSenha());
        when(authenticationManager.authenticate(usernamePasswordAuthenticationToken)).thenThrow(new UsernameNotFoundException("Dados Inválidos"));

        ResponseEntity<TokenDto> autenticar = autenticacaoController.autenticar(loginForm);

        verify(tokenService, never()).gerarToken(any(Authentication.class));
        assertEquals(HttpStatus.BAD_REQUEST, autenticar.getStatusCode());
        assertNull(autenticar.getBody());
    }

    @Test
    public void deveVerificarTokenERetornarUsuarioAutenticado(){
        String headerAuth = "Bearer NovoToken";

        String novoToken = "NovoToken";
        when(tokenService.extrairToken(headerAuth)).thenReturn(novoToken);
        long idUsuario = 1L;
        when(tokenService.getIdUsuario(novoToken)).thenReturn(idUsuario);
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(new Usuario(idUsuario, "usuario", "senha")));

        ResponseEntity<UsuarioDto> usuarioDtoResponseEntity = autenticacaoController.verificarUsuarioAutenticado(headerAuth);

        UsuarioDto usuarioDtoEsperado = new UsuarioDto(1L, "usuario");
        assertEquals(HttpStatus.OK, usuarioDtoResponseEntity.getStatusCode());
        assertEquals(usuarioDtoEsperado, usuarioDtoResponseEntity.getBody());
    }


    @Test
    public void deveVerificarTokenERetornarNotFoundQuandoTokenValidoEUsuarioNaoExisteMais(){
        String headerAuth = "Bearer NovoToken";

        String novoToken = "NovoToken";
        when(tokenService.extrairToken(headerAuth)).thenReturn(novoToken);
        long idUsuario = 1L;
        when(tokenService.getIdUsuario(novoToken)).thenReturn(idUsuario);
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

        ResponseEntity<UsuarioDto> usuarioDtoResponseEntity = autenticacaoController.verificarUsuarioAutenticado(headerAuth);

        assertEquals(HttpStatus.NOT_FOUND, usuarioDtoResponseEntity.getStatusCode());
        assertNull(usuarioDtoResponseEntity.getBody());
    }

    @Test
    public void deveVerificarTokenERetornarBadRequestQuandoForInvalido(){
        String headerAuth = "Bearer NovoToken";

        String novoToken = "NovoToken";
        when(tokenService.extrairToken(headerAuth)).thenReturn(novoToken);
        when(tokenService.getIdUsuario(novoToken)).thenThrow(new RuntimeException());

        ResponseEntity<UsuarioDto> usuarioDtoResponseEntity = autenticacaoController.verificarUsuarioAutenticado(headerAuth);

        assertEquals(HttpStatus.BAD_REQUEST, usuarioDtoResponseEntity.getStatusCode());
        assertNull(usuarioDtoResponseEntity.getBody());
    }

}
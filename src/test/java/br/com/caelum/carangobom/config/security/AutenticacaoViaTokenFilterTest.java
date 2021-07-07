package br.com.caelum.carangobom.config.security;

import br.com.caelum.carangobom.usuario.model.Usuario;
import br.com.caelum.carangobom.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AutenticacaoViaTokenFilterTest {
    @Mock
    private TokenService tokenService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    final String HEADER = "my-header";
    final String TOKEN = "my-token";
    final Long ID_USUARIO = 1L;
    final Usuario USUARIO = new Usuario("Teste", "1234");

    private AutenticacaoViaTokenFilter autenticacaoViaTokenFilter;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        this.autenticacaoViaTokenFilter = new AutenticacaoViaTokenFilter(tokenService,
                usuarioRepository);

        when(request.getHeader("Authorization"))
                .thenReturn(HEADER);
        when(tokenService.extrairToken(HEADER))
                .thenReturn(TOKEN);
    }

    @AfterEach
    public void leave() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternalDeveAutenticarUsuarioSeTokenEhValido() throws ServletException, IOException {
        when(tokenService.isTokenValido(TOKEN))
                .thenReturn(true);
        when(tokenService.getIdUsuario(TOKEN))
                .thenReturn(ID_USUARIO);
        when(usuarioRepository.findById(ID_USUARIO))
                .thenReturn(Optional.of(USUARIO));

        this.autenticacaoViaTokenFilter.doFilterInternal(request, response, filterChain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertEquals(USUARIO, authentication.getPrincipal());
    }

    @Test
    void doFilterInternalNaoDeveAutenticarUsuarioSeTokenNaoEhValido() throws ServletException, IOException {
        when(tokenService.isTokenValido(TOKEN))
                .thenReturn(false);

        this.autenticacaoViaTokenFilter.doFilterInternal(request, response, filterChain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNull(authentication);
    }

    @Test
    void doFilterInternalNaoDeveAutenticarUsuarioSeTokenEhValidoMasUsuarioNaoExiste() throws ServletException, IOException {
        when(tokenService.isTokenValido(TOKEN))
                .thenReturn(true);
        when(tokenService.getIdUsuario(TOKEN))
                .thenReturn(ID_USUARIO);
        when(usuarioRepository.findById(ID_USUARIO))
                .thenReturn(Optional.empty());

        this.autenticacaoViaTokenFilter.doFilterInternal(request, response, filterChain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNull(authentication);
    }
}
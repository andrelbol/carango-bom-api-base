package br.com.caelum.carangobom.config.security;

import br.com.caelum.carangobom.usuario.model.Usuario;
import br.com.caelum.carangobom.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AutenticacaoServiceTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @InjectMocks
    AutenticacaoService autenticacaoService;

    Usuario usuarioTeste;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        usuarioTeste = new Usuario(1L, "teste", "teste");
    }


    @Test
    void deveRetornarDetalhesDoUsuarioExistente() {
        Optional<Usuario> optionalUsuario = Optional.of(usuarioTeste);
        when(usuarioRepository.findByNome(usuarioTeste.getNome())).thenReturn(optionalUsuario);

        UserDetails userDetails = autenticacaoService.loadUserByUsername("teste");
        assertEquals(usuarioTeste.getSenha(), userDetails.getPassword());
        assertEquals(usuarioTeste.getNome(), userDetails.getUsername());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        when(usuarioRepository.findByNome(usuarioTeste.getNome())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> autenticacaoService.loadUserByUsername("teste"));
    }

}
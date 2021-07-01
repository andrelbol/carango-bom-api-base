package br.com.caelum.carangobom.usuario.controller;

import br.com.caelum.carangobom.usuario.controller.dto.UsuarioDto;
import br.com.caelum.carangobom.usuario.controller.form.AlterarSenhaForm;
import br.com.caelum.carangobom.usuario.controller.form.UsuarioForm;
import br.com.caelum.carangobom.usuario.model.Usuario;
import br.com.caelum.carangobom.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UsuarioControllerTest {

    private List<Usuario> usuarios;
    private List<UsuarioDto> usuariosDto;
    private UsuarioController usuarioController;
    private UriComponentsBuilder uriBuilder;
    private PasswordEncoder passwordEncoder;
    private String url;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setup() {
        openMocks(this);
        url = "http://localhost:8080";
        passwordEncoder = new BCryptPasswordEncoder();
        usuarioController = new UsuarioController(usuarioRepository, passwordEncoder);
        usuarios = List.of(new Usuario(1L, "Teste", "123456", "teste@teste.com"));
        usuariosDto = UsuarioDto.converter(usuarios);
        uriBuilder = UriComponentsBuilder.fromUriString(url);
    }

    @Test
    void deveListarTodosUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        assertEquals(usuarioController.listar(), usuariosDto);
    }

    @Test
    void deveListarUsuarioPorId() {
        Usuario usuarioSelecionado = usuarios.get(0);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioSelecionado));

        ResponseEntity<UsuarioDto> resposta = usuarioController.buscarPorId(1L);

        UsuarioDto usuarioRetornado = resposta.getBody();

        assertEquals(usuarioSelecionado.getId(), usuarioRetornado.getId());
        assertEquals(usuarioSelecionado.getNome(), usuarioRetornado.getNome());
        assertEquals(usuarioSelecionado.getEmail(), usuarioRetornado.getEmail());
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void deveRetornarNotFoundQuandoBuscarIdInexistente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<UsuarioDto> resposta = usuarioController.buscarPorId(1L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveResponderCreatedELocationQuandoCadastrarNovoUsuario() {
        long idNovoUsuario = 1L;

        UsuarioForm usuarioForm = new UsuarioForm("LUANA", "123456", "teste@teste.com");

        when(usuarioRepository.save(any(Usuario.class)))
                .then(invocation -> {
                    Usuario usuarioSalvo = invocation.getArgument(0, Usuario.class);
                    System.out.println(usuarioSalvo);
                    usuarioSalvo.setId(idNovoUsuario);
                    return usuarioSalvo;
                });

        ResponseEntity<UsuarioDto> resposta = usuarioController.cadastrar(usuarioForm, uriBuilder);

        UsuarioDto usuarioRetornado = resposta.getBody();

        assertEquals(usuarioForm.getNome(), usuarioRetornado.getNome());
        assertEquals(usuarioForm.getEmail(), usuarioRetornado.getEmail());
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(url + "/usuarios/" + idNovoUsuario, resposta.getHeaders().getLocation().toString());
    }

    @Test
    void deveAlterarNomeQuandoUsuarioExistir() {
        Usuario usuario = usuarios.get(0);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioRepository.getOne(1L))
                .thenReturn(usuario);

        UsuarioForm usuarioForm = new UsuarioForm();
        usuarioForm.setNome("Novo usuário");
        ResponseEntity<UsuarioDto> resposta = usuarioController.alterar(1L, usuarioForm);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());

        UsuarioDto usuarioAlterado = resposta.getBody();
        assertEquals("Novo usuário", usuarioAlterado.getNome());
    }

    @Test
    void naoDeveAlterarUsuarioInexistente() {
        when(usuarioRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        UsuarioForm marcaForm = new UsuarioForm();
        marcaForm.setNome("Novo usuário");
        ResponseEntity<UsuarioDto> resposta = usuarioController.alterar(1L, marcaForm);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveDeletarUsuarioExistente() {
        Usuario usuario = usuarios.get(0);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> resposta = usuarioController.deletar(1L);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());

        verify(usuarioRepository).deleteById(usuario.getId());
    }

    @Test
    void naoDeveDeletarUsuarioInexistente() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        ResponseEntity<Usuario> resposta = usuarioController.deletar(usuario.getId());
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());

        verify(usuarioRepository, never()).deleteById(any());
    }

    @Test
    void deveAlterarSenhaQuandoUsuarioExistirESenhaAnteriorForIgualSenhaCadastrada() {
        long idUsuario = 1L;
        Usuario usuario = new Usuario(idUsuario, "Teste", passwordEncoder.encode("123456"), "teste@teste.com");

        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

        AlterarSenhaForm form = new AlterarSenhaForm("010203", "123456");

        ResponseEntity<UsuarioDto> resposta = usuarioController.alterarSenha(idUsuario, form);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void naoDeveAlterarSenhaQuandoUsuarioNaoExistir() {

        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        AlterarSenhaForm form = new AlterarSenhaForm("010203", "123456");

        ResponseEntity<UsuarioDto> resposta = usuarioController.alterarSenha(1L, form);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void naoDeveAlterarSenhaQuandoASenhaAnteriorInformadaNaoForIgualASenhaCadastradaParaUsuario() {
        long idUsuario = 1L;
        Usuario usuario = new Usuario(idUsuario, "Teste", passwordEncoder.encode("123457"), "teste@teste.com");

        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

        AlterarSenhaForm form = new AlterarSenhaForm("010203", "123456");

        ResponseEntity<UsuarioDto> resposta = usuarioController.alterarSenha(idUsuario, form);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }
}

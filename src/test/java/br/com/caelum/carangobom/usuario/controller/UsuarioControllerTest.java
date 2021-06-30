package br.com.caelum.carangobom.usuario.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.caelum.carangobom.usuario.controller.dto.UsuarioDto;
import br.com.caelum.carangobom.usuario.controller.form.UsuarioForm;
import br.com.caelum.carangobom.usuario.model.Usuario;
import br.com.caelum.carangobom.usuario.repository.UsuarioRepository;

public class UsuarioControllerTest {
	
	private List<Usuario> usuarios;
	private List<UsuarioDto> usuariosDto;
	private UsuarioController usuarioController;
	private UriComponentsBuilder uriBuilder;
	private String url;
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@BeforeEach
    void setup() {
        openMocks(this);
        url = "http://localhost:8080";
        usuarioController = new UsuarioController(usuarioRepository);
        usuarios = List.of(new Usuario(1L, "Teste", "teste@teste.com", "123456"));
        usuariosDto = UsuarioDto.converter(usuarios);
        uriBuilder = UriComponentsBuilder.fromUriString(url);
    }
	
//	@Test
//	void deveListarTodosUsuarios() {
//		when(usuarioRepository.findAll()).thenReturn(usuarios);
//		assertEquals(usuarioController.listar(), usuariosDto);
//		
//	}
//	
//	@Test
//	void deveListarUsuarioPorId() {
//		Usuario usuarioSelecionado = usuarios.get(0);
//		
//		when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioSelecionado));
//		
//		ResponseEntity<UsuarioDto> resposta = usuarioController.buscarPorId(1L);
//		
//		UsuarioDto usuarioRetornado = resposta.getBody();
//		
//		assertEquals(usuarioSelecionado.getId(), usuarioRetornado.getId());
//		assertEquals(usuarioSelecionado.getNome(), usuarioRetornado.getNome());
//		assertEquals(usuarioSelecionado.getEmail(), usuarioRetornado.getEmail());
//		assertEquals(resposta.getStatusCode(), HttpStatus.OK);
//	}
//	
//	@Test
//	void deveRetornarNotFoundQuandoBuscarIdInexistente() {		
//		when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
//		ResponseEntity<UsuarioDto> resposta = usuarioController.buscarPorId(1L);
//		
//		assertEquals(resposta.getStatusCode(), HttpStatus.NOT_FOUND);		
//	}

	@Test
	void deveResponderCreatedELocationQuandoCadastrarNovoUsuario() {
		long idNovoUsuario = 1;
		
		UsuarioForm usuarioForm = new UsuarioForm("LUANA", "123456", "teste@teste.com");
		
		when(usuarioRepository.save(usuarioForm.converter()))
        .then(invocation -> {
            Usuario usuarioSalvo = invocation.getArgument(0, Usuario.class);
            usuarioSalvo.setId(idNovoUsuario);
            return usuarioSalvo;
        });
		
		
		ResponseEntity<UsuarioDto> resposta = usuarioController.cadastrar(usuarioForm, uriBuilder);
		
		UsuarioDto usuarioRetornado = resposta.getBody();
		
		assertEquals(usuarioForm.getNome(), usuarioRetornado.getNome());
		assertEquals(usuarioForm.getEmail(), usuarioRetornado.getEmail());
		assertEquals(resposta.getStatusCode(), HttpStatus.CREATED);
        assertEquals(url + "/usuarios/" + idNovoUsuario, resposta.getHeaders().getLocation().toString());
	}
}

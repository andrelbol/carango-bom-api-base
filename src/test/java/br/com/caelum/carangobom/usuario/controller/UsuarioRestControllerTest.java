package br.com.caelum.carangobom.usuario.controller;

import br.com.caelum.carangobom.usuario.model.Usuario;
import br.com.caelum.carangobom.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsuarioController.class)
class UsuarioRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private UriComponentsBuilder uriBuilder;

    @MockBean
    private UsuarioRepository usuarioRepository;

    private List<Usuario> usuarios;

    @BeforeEach
    public void setup() {
        openMocks(this);
        String url = "http://localhost:8080";
        usuarios = List.of(new Usuario(1L, "Teste", "123456", "teste@teste.com"));
        uriBuilder = UriComponentsBuilder.fromUriString(url);
    }


    @Test
    void deveRetornarListaQuandoHouverResultados() throws Exception {
        given(usuarioRepository.findAll())
                .willReturn(usuarios);

        this.mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(usuarios.size())));
    }

    @Test
    void deveValidarFormatoDeEntradaNoCadastro() throws Exception {
        given(usuarioRepository.save(any()))
                .willReturn(Optional.of(usuarios.get(0)));

        this.mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1,\"nome\": \"Teste\",\"email\": \"teste@teste.com\"}"))
                .andExpect(status().isBadRequest());
    }

}

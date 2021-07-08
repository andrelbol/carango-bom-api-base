package br.com.caelum.carangobom.usuario.controller;

import br.com.caelum.carangobom.usuario.model.Usuario;
import br.com.caelum.carangobom.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UsuarioRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private UriComponentsBuilder uriBuilder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private List<Usuario> usuarios;

    @BeforeEach
    public void setup() {
        openMocks(this);
        String url = "http://localhost:8080";
        uriBuilder = UriComponentsBuilder.fromUriString(url);
    }


    @Test
    void deveRetornarListaQuandoHouverResultados() throws Exception {
        usuarios = List.of(new Usuario("Teste", "123456"));
        usuarioRepository.saveAll(usuarios);

        this.mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(usuarios.size())));

        usuarioRepository.deleteAll();
    }

    @Test
    void deveValidarFormatoDeEntradaNoCadastro() throws Exception {

        this.mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1,\"nome\": \"Teste\",\"email\": \"teste@teste.com\"}"))
                .andExpect(status().isBadRequest());
    }

}

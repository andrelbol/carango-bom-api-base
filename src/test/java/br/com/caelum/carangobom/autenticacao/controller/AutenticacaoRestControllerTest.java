package br.com.caelum.carangobom.autenticacao.controller;

import br.com.caelum.carangobom.autenticacao.controller.dto.TokenDto;
import br.com.caelum.carangobom.autenticacao.controller.form.LoginForm;
import br.com.caelum.carangobom.marca.model.Marca;
import br.com.caelum.carangobom.marca.repository.MarcaRepository;
import br.com.caelum.carangobom.usuario.controller.UsuarioController;
import br.com.caelum.carangobom.usuario.controller.dto.UsuarioDto;
import br.com.caelum.carangobom.usuario.controller.form.UsuarioForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySources(
        @TestPropertySource("/application-test.properties")
)
class AutenticacaoRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private UriComponentsBuilder uriBuilder;

    @Autowired
    private UsuarioController usuarioController;

    @Autowired
    private AutenticacaoController autenticacaoController;


    @BeforeEach
    public void configuraMock() {
        openMocks(this);

        uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");
    }

    @Test
    void deveAutenticarUsuarioRecemCadastrado() throws Exception {

        UsuarioForm usuarioForm = new UsuarioForm("UsuarioTesteAuth", "123456");
        ResponseEntity<UsuarioDto> cadastrar = usuarioController.cadastrar(usuarioForm, uriBuilder);
        UsuarioDto usuario = cadastrar.getBody();

        MvcResult resultadoAuth = this.mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"nome\": \"UsuarioTesteAuth\",\n" +
                        "  \"senha\": \"123456\"\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", is("Bearer")))
                .andExpect(jsonPath("$.token").isString())
                .andReturn();
    }

    @Test
    void naoDeveAutenticarUsuarioInvalido() throws Exception {

        UsuarioForm usuarioForm = new UsuarioForm("UsuarioTesteInvalido", "123456");
        ResponseEntity<UsuarioDto> cadastrar = usuarioController.cadastrar(usuarioForm, uriBuilder);
        UsuarioDto usuario = cadastrar.getBody();

        this.mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"nome\": \"UsuarioTesteInvalido\",\n" +
                        "  \"senha\": \"1234567\"\n" +
                        "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void deveVerificarUsuario() throws Exception {

        UsuarioForm usuarioForm = new UsuarioForm("usuarioVerificarToken", "123456");
        ResponseEntity<UsuarioDto> cadastrar = usuarioController.cadastrar(usuarioForm, uriBuilder);
        UsuarioDto usuario = cadastrar.getBody();

        LoginForm form = new LoginForm();
        form.setNome(usuarioForm.getNome());
        form.setSenha(usuarioForm.getSenha());
        TokenDto autenticar = autenticacaoController.autenticar(form).getBody();

        this.mockMvc.perform(get("/auth")
                .header("Authorization", autenticar.getTipo() + " " + autenticar.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(usuario.getId().intValue())))
                .andExpect(jsonPath("$.nome", is(usuario.getNome())));
    }

}
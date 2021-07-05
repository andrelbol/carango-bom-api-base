package br.com.caelum.carangobom.marca.controller;

import br.com.caelum.carangobom.marca.model.Marca;
import br.com.caelum.carangobom.marca.repository.MarcaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MarcaController.class)
@ActiveProfiles("test")
class MarcaRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private UriComponentsBuilder uriBuilder;

//  ToDo: Parar de mockar as repositories;
    @MockBean
    private MarcaRepository marcaRepository;

    private List<Marca> marcas;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);
        marcas = List.of(
                new Marca(1L, "Audi"),
                new Marca(2L, "BMW"),
                new Marca(3L, "Fiat"));
        uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");
    }

    @Test
    void deveRetornarListaQuandoHouverResultados() throws Exception {
        given(marcaRepository.findByOrderByNome())
                .willReturn(marcas);

        this.mockMvc.perform(get("/marcas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(marcas.size())));

    }

    @Test
    void deveValidarFormatoDeEntradaNoCadastro() throws Exception {
        given(marcaRepository.save(any()))
                .willReturn(new Marca(1L, "teste"));

        this.mockMvc.perform(post("/marcas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"teste\"}"))
                .andExpect(status().isBadRequest());
    }

}
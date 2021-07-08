package br.com.caelum.carangobom.marca.controller;

import br.com.caelum.carangobom.marca.model.Marca;
import br.com.caelum.carangobom.marca.repository.MarcaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
class MarcaRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private UriComponentsBuilder uriBuilder;

    @Autowired
    private MarcaRepository marcaRepository;

    private List<Marca> marcas;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);
        marcas = List.of(
                new Marca("Audi"),
                new Marca("BMW"),
                new Marca("Fiat"));
        uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");
    }

    @Test
    void deveRetornarListaQuandoHouverResultados() throws Exception {

        marcaRepository.saveAll(marcas);

        this.mockMvc.perform(get("/marcas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(marcas.size())));

        marcaRepository.deleteAll();
    }

    @Test
    void deveValidarFormatoDeEntradaNoCadastro() throws Exception {

        this.mockMvc.perform(post("/marcas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"teste\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarDashboardQuandoHouverResultados() throws Exception {

        marcaRepository.saveAll(marcas);

        this.mockMvc.perform(get("/marcas/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(marcas.size())));

        marcaRepository.deleteAll();
    }

}
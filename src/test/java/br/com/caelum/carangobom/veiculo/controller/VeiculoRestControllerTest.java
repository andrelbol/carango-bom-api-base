package br.com.caelum.carangobom.veiculo.controller;

import br.com.caelum.carangobom.marca.model.Marca;
import br.com.caelum.carangobom.marca.repository.MarcaRepository;
import br.com.caelum.carangobom.veiculo.model.Veiculo;
import br.com.caelum.carangobom.veiculo.repository.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = VeiculoController.class)
@ActiveProfiles("test")
class VeiculoRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private UriComponentsBuilder uriBuilder;

    @MockBean
    private MarcaRepository marcaRepository;

    @MockBean
    private VeiculoRepository veiculoRepository;

    private List<Veiculo> veiculos;

    @BeforeEach
    void setup() {
        openMocks(this);
        veiculos = List.of(
                new Veiculo(1L, new Marca(1L, "VW"), 2021, "Gol", new BigDecimal("25000"))
        );
        uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");
    }

    @Test
    void deveRetornarListaQuandoHouverResultados() throws Exception {
        given(veiculoRepository.findByOrderByModelo()).willReturn(veiculos);

        this.mockMvc.perform(get("/veiculos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(veiculos.size())));

    }

    @Test
    void deveValidarFormatoDeEntradaNoCadastro() throws Exception {
        Veiculo gol = new Veiculo(1L, new Marca(1L, "VW"), 2021, "Gol", new BigDecimal("25000"));
        given(veiculoRepository.save(any())).willReturn(gol);

        this.mockMvc.perform(post("/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"teste\"}"))
                .andExpect(status().isBadRequest());
    }
    /**/
}

package br.com.caelum.carangobom.marca.controller;

import br.com.caelum.carangobom.marca.controller.dto.DashboardMarcaDto;
import br.com.caelum.carangobom.marca.controller.dto.MarcaDto;
import br.com.caelum.carangobom.marca.controller.form.MarcaForm;
import br.com.caelum.carangobom.marca.model.DashboardMarcaProjecao;
import br.com.caelum.carangobom.marca.model.Marca;
import br.com.caelum.carangobom.marca.repository.MarcaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class MarcaControllerTest {

    private MarcaController marcaController;
    private UriComponentsBuilder uriBuilder;

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private DashboardMarcaProjecao primeiroItem;
    @Mock
    private DashboardMarcaProjecao segundoItem;
    @Mock
    private DashboardMarcaProjecao terceiroItem;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);

        marcaController = new MarcaController(marcaRepository);
        uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");
    }

    @Test
    void deveRetornarListaQuandoHouverResultados() {
        List<Marca> marcas = List.of(
                new Marca(1L, "Audi"),
                new Marca(2L, "BMW"),
                new Marca(3L, "Fiat")
        );

        when(marcaRepository.findByOrderByNome())
                .thenReturn(marcas);

        List<MarcaDto> marcasDtos = MarcaDto.converter(marcas);
        List<MarcaDto> resultado = marcaController.lista();
        assertEquals(marcasDtos, resultado);
    }

    @Test
    void deveRetornarMarcaPeloId() {
        Marca audi = new Marca(1L, "Audi");

        when(marcaRepository.findById(1L))
                .thenReturn(Optional.of(audi));

        ResponseEntity<MarcaDto> resposta = marcaController.id(1L);
        MarcaDto marcaRetornada = resposta.getBody();

        assertEquals(audi.getId(), marcaRetornada.getId());
        assertEquals(audi.getNome(), marcaRetornada.getNome());
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void deveRetornarNotFoundQuandoRecuperarMarcaComIdInexistente() {
        when(marcaRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        ResponseEntity<MarcaDto> resposta = marcaController.id(1L);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveResponderCreatedELocationQuandoCadastrarMarca() {
        MarcaForm nova = new MarcaForm();
        nova.setNome("Ferrari");

        when(marcaRepository.save(nova.converter()))
                .then(invocation -> {
                    Marca marcaSalva = invocation.getArgument(0, Marca.class);
                    marcaSalva.setId(1L);

                    return marcaSalva;
                });

        ResponseEntity<MarcaDto> resposta = marcaController.cadastra(nova, uriBuilder);

        assertEquals(nova.getNome(), resposta.getBody().getNome());
        assertEquals(1L, resposta.getBody().getId());
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals("http://localhost:8080/marcas/1", resposta.getHeaders().getLocation().toString());
    }

    @Test
    void deveAlterarNomeQuandoMarcaExistir() {
        Marca audi = new Marca(1L, "Audi");

        when(marcaRepository.findById(1L))
                .thenReturn(Optional.of(audi));

        when(marcaRepository.getOne(1L))
                .thenReturn(audi);

        MarcaForm marcaForm = new MarcaForm();
        marcaForm.setNome("NOVA Audi");
        ResponseEntity<MarcaDto> resposta = marcaController.altera(1L, marcaForm);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());

        MarcaDto marcaAlterada = resposta.getBody();
        assertEquals("NOVA Audi", marcaAlterada.getNome());
    }

    @Test
    void naoDeveAlterarMarcaInexistente() {
        when(marcaRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        MarcaForm marcaForm = new MarcaForm();
        marcaForm.setNome("NOVA Audi");
        ResponseEntity<MarcaDto> resposta = marcaController.altera(1L, marcaForm);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveDeletarMarcaExistente() {
        Marca audi = new Marca(1L, "Audi");

        when(marcaRepository.findById(1L))
                .thenReturn(Optional.of(audi));

        ResponseEntity<Marca> resposta = marcaController.deleta(1L);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());

        verify(marcaRepository).deleteById(audi.getId());
    }

    @Test
    void naoDeveDeletarMarcaInexistente() {
        Marca marca = new Marca();
        marca.setId(1L);
        when(marcaRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        ResponseEntity<Marca> resposta = marcaController.deleta(marca.getId());
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());

        verify(marcaRepository, never()).deleteById(any());
    }

    @Test
    void deveRetornarDashboardMarcasQuandoHouverResultados() {
        List<DashboardMarcaProjecao> dashboard = List.of(
                primeiroItem,
                primeiroItem
        );

        when(primeiroItem.getMarca()).thenReturn(new Marca(1L, "Audi"));
        when(primeiroItem.getQuantidadeVeiculos()).thenReturn(2);
        when(primeiroItem.getValorTotalVeiculos()).thenReturn(new BigDecimal("500000"));

        when(marcaRepository.getSumarioMarcas())
                .thenReturn(dashboard);

        List<DashboardMarcaDto> marcasDtos = DashboardMarcaDto.converter(dashboard);
        List<DashboardMarcaDto> resultado = marcaController.consultaDashboardMarcas();
        assertEquals(marcasDtos, resultado);
    }


}
package br.com.caelum.carangobom.veiculo.controller;

import br.com.caelum.carangobom.marca.model.Marca;
import br.com.caelum.carangobom.marca.repository.MarcaRepository;
import br.com.caelum.carangobom.veiculo.controller.dto.VeiculoDto;
import br.com.caelum.carangobom.veiculo.controller.form.VeiculoForm;
import br.com.caelum.carangobom.veiculo.model.Veiculo;
import br.com.caelum.carangobom.veiculo.repository.VeiculoRepository;
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

class VeiculoControllerTest {

    private List<Veiculo> veiculos;
    private List<VeiculoDto> veiculoDtos;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private MarcaRepository marcaRepository;

    private VeiculoController veiculoController;
    private UriComponentsBuilder uriBuilder;
    private String url;

    @BeforeEach
    void setup() {
        openMocks(this);
        url = "http://localhost:8080";
        veiculoController = new VeiculoController(veiculoRepository, marcaRepository);
        veiculos = List.of(new Veiculo(1L, new Marca(1L, "Fiat"), 2021, "Uno", new BigDecimal("4500.0")));
        veiculoDtos = VeiculoDto.converter(veiculos);
        uriBuilder = UriComponentsBuilder.fromUriString(url);
    }

    @Test
    void deveListarTodosVeiculo() {
        when(veiculoRepository.findByOrderByModelo()).thenReturn(veiculos);
        assertEquals(veiculoController.listar(), veiculoDtos);
    }

    @Test
    void deveRetornarSomenteUmRegistroQuandoIdExistente() {
        Veiculo veiculoSelecionado = veiculos.get(0);

        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoSelecionado));

        ResponseEntity<VeiculoDto> resposta = veiculoController.listarPorId(1L);
        VeiculoDto veiculoRetornado = resposta.getBody();

        assertEquals(veiculoSelecionado.getId(), veiculoRetornado.getId());
        assertEquals(veiculoSelecionado.getModelo(), veiculoRetornado.getModelo());
        assertEquals(veiculoSelecionado.getMarca().getNome(), veiculoRetornado.getMarca());
        assertEquals(veiculoSelecionado.getAno(), veiculoRetornado.getAno());
        assertEquals(veiculoSelecionado.getValor(), veiculoRetornado.getValor());
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }


    @Test
    void deveRetornarNotFoundQuandoBuscarIdInexistente() {
        Veiculo veiculoSelecionado = veiculos.get(0);

        when(veiculoRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<VeiculoDto> resposta = veiculoController.listarPorId(1L);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveRetornarNotFoundQuandoTentarCadastrarVeiculoComMarcaIdInesxistente() {
        when(marcaRepository.findById(anyLong())).thenReturn(Optional.empty());
        VeiculoForm novoCadastro = new VeiculoForm(1l, 2021, "Gol", new BigDecimal("10000.0"));
        ResponseEntity<VeiculoDto> resposta = veiculoController.cadastrar(novoCadastro, uriBuilder);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveResponderCreatedELocationQuandoCadastrarNovoVeiculo() {
        long idNovoVeiculo = 1;
        Marca marca = new Marca(1l, "VW");

        VeiculoForm novoCadastro = new VeiculoForm(1l, 2021, "Gol", new BigDecimal("10000.0"));

        when(veiculoRepository.save(novoCadastro.converter(marca)))
                .then(invocation -> {
                    Veiculo veiculoSalvo = invocation.getArgument(0, Veiculo.class);
                    veiculoSalvo.setId(idNovoVeiculo);
                    return veiculoSalvo;
                });

        when(marcaRepository.findById(anyLong())).thenReturn(Optional.of(marca));

        ResponseEntity<VeiculoDto> resposta = veiculoController.cadastrar(novoCadastro, uriBuilder);
        VeiculoDto veiculoCriado = resposta.getBody();

        assertEquals(novoCadastro.getModelo(), veiculoCriado.getModelo());
        assertEquals(marca.getNome(), veiculoCriado.getMarca());
        assertEquals(novoCadastro.getAno(), veiculoCriado.getAno());
        assertEquals(novoCadastro.getValor(), veiculoCriado.getValor());
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(url + "/veiculos/" + idNovoVeiculo, resposta.getHeaders().getLocation().toString());
    }

    @Test
    void deveAlterarNomeQuandoVeiculoExistir() {
        Marca marca = new Marca(1L, "VW");

        Veiculo veiculoAntesDaAlteracao = new Veiculo(1l, marca, 2021, "Gol", new BigDecimal("10000.0"));

        VeiculoForm veiculoForm = new VeiculoForm(1l, 2021, "Gol Alterado", new BigDecimal("10000.0"));

        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));

        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculoAntesDaAlteracao));

        ResponseEntity<VeiculoDto> resposta = veiculoController.alterar(1L, veiculoForm);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());

        VeiculoDto veiculoAlterado = resposta.getBody();

        assertEquals(veiculoForm.getModelo(), veiculoAlterado.getModelo());
        assertEquals(marca.getNome(), veiculoAlterado.getMarca());
        assertEquals(veiculoForm.getAno(), veiculoAlterado.getAno());
        assertEquals(veiculoForm.getValor(), veiculoAlterado.getValor());
    }

    @Test
    void naoDeveAlterarVeiculoQuandoInformadaUmaMarcaInexistente() {
        when(marcaRepository.findById(anyLong())).thenReturn(Optional.empty());

        VeiculoForm veiculoForm = new VeiculoForm();
        ResponseEntity<VeiculoDto> resposta = veiculoController.alterar(1L, veiculoForm);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void naoDeveAlterarVeiculoInexistente() {
        VeiculoForm veiculoForm = new VeiculoForm();
        veiculoForm.setMarcaId(1l);

        when(marcaRepository.findById(anyLong())).thenReturn(Optional.of(new Marca(1l, "VW")));
        when(veiculoRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<VeiculoDto> resposta = veiculoController.alterar(1L, veiculoForm);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveDeletarVeiculoExistente() {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(1l);

        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        ResponseEntity resposta = veiculoController.deletar(1L);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        verify(veiculoRepository).deleteById(veiculo.getId());
    }

    @Test
    void deveRetornarNotFoundAoTentarExcluirVeiculoInexistente() {
        when(veiculoRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Veiculo> resposta = veiculoController.deletar(1l);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());

        verify(veiculoRepository, never()).deleteById(any());
    }
}

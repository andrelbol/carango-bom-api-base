package br.com.caelum.carangobom.veiculo.model;

import br.com.caelum.carangobom.marca.model.Marca;
import br.com.caelum.carangobom.veiculo.controller.dto.VeiculoDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class VeiculoTest {

    private Veiculo veiculo2;
    private Veiculo veiculo1;

    @BeforeEach
    public void setup(){
        Marca marca = new Marca(1l, "VW");
        veiculo1 = new Veiculo(1l, marca, 2021, "Gol", new BigDecimal("25000"));
        veiculo2 = new Veiculo(1l, marca, 2021, "Gol", new BigDecimal("25000"));
    }

    @Test
    void deveGerarHashcodeIgualParaDoisVeiculosComOsMesmosDados() {
        assertEquals(veiculo1.hashCode(),veiculo2.hashCode());
    }

    @Test
    void deveGerarHashcodeIgual31QuandoIdNaoInformado() {
        assertEquals(new Veiculo().hashCode(),31);
    }

    @Test
    void deveConsiderarIgualDoisVeiculosComDadoIdenticos() {
        assertTrue(veiculo1.equals(veiculo2));
    }

    @Test
    void naoDeveRetornarVerdadeiroAoCampararVeiculoComUmObjetoNulo(){
        assertFalse(veiculo1.equals(null));
    }

    @Test
    void umObjetoSerIgualAEleMesmo(){
        assertTrue(veiculo1.equals(veiculo1));
    }

    @Test
    void naoDeveSerIgualQuandoComparadoAOutroObjeto(){
        assertFalse(veiculo1.equals(new Object()));
    }

    @Test
    void naoDeveSerIgualQuandoIdDosVeiculosForemDiferentes(){
        Veiculo diferente = new Veiculo();
        diferente.setId(2l);
        assertFalse(veiculo1.equals(diferente));
    }

    @Test
    void naoDeveSerIgualQuandoIdDoVeiculoDaComparacaoEstiverNulo(){
        Veiculo diferente = new Veiculo();
        diferente.setId(2l);
        assertFalse(new Veiculo().equals(diferente));
    }
}
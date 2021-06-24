package br.com.caelum.carangobom.veiculo.controller.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class VeiculoDtoTest {

    @Test
    void deveGerarHashcodeIgualParaDoisVeiculosComOsMesmosDados() {
        VeiculoDto dto1 = new VeiculoDto(1l,"VW", 2021,"Gol", new BigDecimal("25000"));
        VeiculoDto dto2 = new VeiculoDto(1l,"VW", 2021,"Gol", new BigDecimal("25000"));

        assertEquals(dto1.hashCode(),dto2.hashCode());
    }
}
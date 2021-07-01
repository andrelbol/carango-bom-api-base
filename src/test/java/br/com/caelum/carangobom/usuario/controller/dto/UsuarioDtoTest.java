package br.com.caelum.carangobom.usuario.controller.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDtoTest {

    @Test
    void deveGerarHashCodesIguaisDoisObjetosComOsMesmosDados() {
        UsuarioDto usuario1 = new UsuarioDto(1L, "Teste", "teste@teste.com");
        UsuarioDto usuario2 = new UsuarioDto(1L, "Teste", "teste@teste.com");

        assertEquals(usuario1.hashCode(),usuario2.hashCode());
    }
}
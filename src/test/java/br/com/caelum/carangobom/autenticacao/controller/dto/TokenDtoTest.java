package br.com.caelum.carangobom.autenticacao.controller.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenDtoTest {

    @Test
    void testEquals() {
        TokenDto tokenDto = new TokenDto("teste", "teste");
        TokenDto tokenDuplicado = tokenDto;
        TokenDto tokenIgual = new TokenDto("teste", "teste");
        TokenDto tokenTipoDiferente = new TokenDto("teste", "diferente");
        TokenDto tokenNomeDiferente = new TokenDto("diferente", "teste");
        Object outro = new Object();
        Object nulo = null;
        assertEquals(tokenDto, tokenDuplicado);
        assertEquals(tokenDto, tokenIgual);
        assertNotEquals(tokenDto, tokenTipoDiferente);
        assertNotEquals(tokenDto, tokenNomeDiferente);
        assertNotEquals(tokenDto, outro);
        assertNotEquals(tokenDto, nulo);

    }

    @Test
    void testHashCode() {
        TokenDto tokenDto = new TokenDto("teste", "teste");
        TokenDto outroToken = new TokenDto("teste", "teste");
        assertEquals(tokenDto.hashCode(), outroToken.hashCode());
    }
}
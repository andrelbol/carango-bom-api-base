package br.com.caelum.carangobom.autenticacao.controller.dto;

import java.util.Objects;

public class TokenDto {

    private final String token;
    private final String tipo;

    public TokenDto(String token, String tipo) {
        this.token = token;
        this.tipo = tipo;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenDto tokenDto = (TokenDto) o;
        return Objects.equals(token, tokenDto.token) && Objects.equals(tipo, tokenDto.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, tipo);
    }
}

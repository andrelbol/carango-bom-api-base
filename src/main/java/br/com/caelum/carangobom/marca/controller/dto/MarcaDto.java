package br.com.caelum.carangobom.marca.controller.dto;

import br.com.caelum.carangobom.marca.model.Marca;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MarcaDto {

    private Long id;
    private String nome;

    public MarcaDto(Marca marca) {
        this.id = marca.getId();
        this.nome = marca.getNome();
    }

    public static List<MarcaDto> converter(List<Marca> marcas) {
        return marcas.stream().map(MarcaDto::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarcaDto marcaDto = (MarcaDto) o;
        return Objects.equals(id, marcaDto.id) && Objects.equals(nome, marcaDto.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }
}

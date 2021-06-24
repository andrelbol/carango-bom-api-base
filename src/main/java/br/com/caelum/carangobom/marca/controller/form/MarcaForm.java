package br.com.caelum.carangobom.marca.controller.form;

import br.com.caelum.carangobom.marca.model.Marca;
import br.com.caelum.carangobom.marca.repository.MarcaRepository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MarcaForm {

    @NotBlank
    @NotNull
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Marca converter() {
        return new Marca(nome);
    }

    public Marca atualizar(Long id, MarcaRepository marcaRepository) {
        Marca marca = marcaRepository.getOne(id);
        marca.setNome(this.nome);
        return marca;
    }
}

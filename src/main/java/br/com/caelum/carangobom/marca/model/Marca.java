package br.com.caelum.carangobom.marca.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Marca {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, message = "Deve ter {min} ou mais caracteres.")
    private String nome;

    public Marca() {

    }

    public Marca(String nome) {
        this(null, nome);
    }

    public Marca(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Marca marca = (Marca) o;
        return Objects.equals(id, marca.id) && Objects.equals(nome, marca.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }
}

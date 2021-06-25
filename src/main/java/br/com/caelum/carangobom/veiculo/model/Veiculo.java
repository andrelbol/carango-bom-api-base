package br.com.caelum.carangobom.veiculo.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.caelum.carangobom.marca.model.Marca;

@Entity
public class Veiculo {

	public Veiculo() {
	}

	public Veiculo(Long id, Marca marca, int ano, String modelo, BigDecimal valor) {
		this.id = id;
		this.marca = marca;
		this.ano = ano;
		this.modelo = modelo;
		this.valor = valor;
	}

	public Veiculo(Marca marca, int ano, String modelo, BigDecimal valor) {
		this(null, marca, ano, modelo, valor);
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "marca_id")
	private Marca marca;

	@NotNull
	private int ano;

	@NotNull
	@Size(min = 2, message = "Deve ter {min} ou mais caracteres.")
	private String modelo;

	@NotNull
	private BigDecimal valor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Veiculo veiculo = (Veiculo) o;
		return ano == veiculo.ano && Objects.equals(id, veiculo.id) && Objects.equals(marca, veiculo.marca) && Objects.equals(modelo, veiculo.modelo) && Objects.equals(valor, veiculo.valor);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, marca, ano, modelo, valor);
	}
}

package br.com.caelum.carangobom.veiculo.controller.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import br.com.caelum.carangobom.veiculo.model.Veiculo;

public class VeiculoDto {

	private Long id;
	private String marca;
	private int ano;
	private String modelo;
	private BigDecimal valor;

	public VeiculoDto(Long id, String marca, int ano, String modelo, BigDecimal valor) {
		this.id = id;
		this.marca = marca;
		this.ano = ano;
		this.modelo = modelo;
		this.valor = valor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
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

	public static VeiculoDto parse(Veiculo veiculo) {
		return new VeiculoDto(veiculo.getId(), veiculo.getMarca().getNome(), veiculo.getAno(), veiculo.getModelo(),
				veiculo.getValor());
	}

	public static List<VeiculoDto> converter(List<Veiculo> veiculos) {
		return veiculos.stream().map(VeiculoDto::parse).collect(Collectors.toList());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		VeiculoDto that = (VeiculoDto) o;

		if (ano != that.ano) return false;
		if (!id.equals(that.id)) return false;
		if (!marca.equals(that.marca)) return false;
		if (!modelo.equals(that.modelo)) return false;
		return valor.equals(that.valor);
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + marca.hashCode();
		result = 31 * result + ano;
		result = 31 * result + modelo.hashCode();
		result = 31 * result + valor.hashCode();
		return result;
	}
}

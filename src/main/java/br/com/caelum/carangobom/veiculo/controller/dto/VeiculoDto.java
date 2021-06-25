package br.com.caelum.carangobom.veiculo.controller.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import br.com.caelum.carangobom.veiculo.model.Veiculo;

public class VeiculoDto {

	private Long id;
	private String marca;
	private Long marcaId;
	private int ano;
	private String modelo;
	private BigDecimal valor;

	public VeiculoDto(Long id, String marca, Long marcaId, int ano, String modelo, BigDecimal valor) {
		this.id = id;
		this.marca = marca;
		this.ano = ano;
		this.modelo = modelo;
		this.valor = valor;
		this.marcaId = marcaId;
	}

	public Long getId() {
		return id;
	}

	public String getMarca() {
		return marca;
	}

	public Long getMarcaId() {
		return marcaId;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public int getAno() {
		return ano;
	}

	public String getModelo() {
		return modelo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public static VeiculoDto parse(Veiculo veiculo) {
		return new VeiculoDto(
				veiculo.getId(),
				veiculo.getMarca().getNome(),
				veiculo.getMarca().getId(),
				veiculo.getAno(), veiculo.getModelo(),
				veiculo.getValor()
		);
	}

	public static List<VeiculoDto> converter(List<Veiculo> veiculos) {
		return veiculos.stream().map(VeiculoDto::parse).collect(Collectors.toList());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		VeiculoDto that = (VeiculoDto) o;

		if (!id.equals(that.id)) return false;
		return valor.equals(that.valor);
	}

	@Override
	public int hashCode() {
		return Objects.hash(marca.hashCode(),ano,modelo, valor, id);
	}
}

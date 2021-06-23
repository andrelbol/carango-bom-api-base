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
}

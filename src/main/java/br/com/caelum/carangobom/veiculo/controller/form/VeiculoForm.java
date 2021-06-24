package br.com.caelum.carangobom.veiculo.controller.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import br.com.caelum.carangobom.marca.model.Marca;
import br.com.caelum.carangobom.veiculo.model.Veiculo;

public class VeiculoForm {

	public VeiculoForm(){}
	public VeiculoForm(Long marcaId, int ano, String modelo, BigDecimal valor) {
		this.marcaId = marcaId;
		this.ano = ano;
		this.modelo = modelo;
		this.valor = valor;
	}

	@NotNull
	private Long marcaId;

	@NotNull
	private int ano;

	@NotNull
	private String modelo;

	@NotNull
	private BigDecimal valor;

	public Long getMarcaId() {
		return marcaId;
	}

	public void setMarcaId(Long marcaId) {
		this.marcaId = marcaId;
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

	public Veiculo converter(Marca marca) {
		return new Veiculo(marca, ano, modelo, valor);
	}

	public Veiculo atualizar(Veiculo veiculo, Marca marca) {
		veiculo.setMarca(marca);
		veiculo.setAno(ano);
		veiculo.setModelo(modelo);
		veiculo.setValor(valor);
		return veiculo;
	}

}

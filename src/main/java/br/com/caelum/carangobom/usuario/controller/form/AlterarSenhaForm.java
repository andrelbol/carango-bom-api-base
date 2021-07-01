package br.com.caelum.carangobom.usuario.controller.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.caelum.carangobom.usuario.model.Usuario;

public class AlterarSenhaForm {
	
	
	public AlterarSenhaForm() {}
	
	public AlterarSenhaForm(String novaSenha, String senhaAnterior) {
		this.novaSenha = novaSenha;
		this.senhaAnterior = senhaAnterior;
	}
	
	@NotBlank
	@Size(min = 6, message = "Deve ter {min} ou mais caracteres.")
	private String novaSenha;
	
	@NotBlank
	private String senhaAnterior;

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getSenhaAnterior() {
		return senhaAnterior;
	}

	public void setSenhaAnterior(String senhaAnterior) {
		this.senhaAnterior = senhaAnterior;
	}
	
	public Usuario atualizarSenha(Usuario usuario) {
		usuario.setSenha(novaSenha);
		
		return usuario;
	}

}

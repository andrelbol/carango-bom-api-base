package br.com.caelum.carangobom.usuario.controller.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.caelum.carangobom.usuario.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UsuarioForm {
	
	public UsuarioForm() {}
	
	public UsuarioForm(String nome, String senha) {
		this.nome = nome;
		this.senha = senha;
	}
	
	@NotBlank
	private String nome;
	
	@NotBlank
	@Size(min = 6, message = "Deve ter {min} ou mais caracteres.")
	private String senha;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Usuario converter(PasswordEncoder passwordEncoder) {
		return new Usuario(nome, passwordEncoder.encode(senha));
	}

	public Usuario atualizar(Usuario usuario) {
		usuario.setNome(nome);

		return usuario;
	}


}

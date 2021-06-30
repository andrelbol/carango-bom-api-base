package br.com.caelum.carangobom.usuario.controller.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.caelum.carangobom.usuario.model.Usuario;
import br.com.caelum.carangobom.usuario.service.CriptografiaService;

public class UsuarioForm {
	
	public UsuarioForm() {}
	
	public UsuarioForm(Long id, String nome, String senha, String email) {
		super();
		this.id = id;
		this.nome = nome;
		this.senha = senha;
		this.email = email;
	}

	private Long id;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	@Size(min = 6, message = "Deve ter {min} ou mais caracteres.")
	private String senha;
	
	@NotBlank
	@Email
	private String email;

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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Usuario converter() {
		return new Usuario(id, nome, CriptografiaService.criptografarSenha(senha), email);
	}

	public Usuario atualizar(Usuario usuario) {
		usuario.setNome(nome);
		usuario.setEmail(email);
		
		return usuario;
	}


}

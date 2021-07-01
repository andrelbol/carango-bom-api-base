package br.com.caelum.carangobom.usuario.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Usuario {

	public Usuario() {}
	
	public Usuario(String nome, String senha, String email) {
		this(null, nome, senha, email);		
	}
	
    public Usuario(Long id, String nome, String senha, String email) {
		super();
		this.id = id;
		this.nome = nome;
		this.senha = senha;
		this.email = email;
	}

	@Id
    @GeneratedValue(strategy = IDENTITY)
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) 
        		&& Objects.equals(nome, usuario.nome) 
        		&& Objects.equals(senha, usuario.senha)
        		&& Objects.equals(email, usuario.email);
	}
}

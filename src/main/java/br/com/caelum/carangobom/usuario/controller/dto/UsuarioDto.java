package br.com.caelum.carangobom.usuario.controller.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import br.com.caelum.carangobom.usuario.model.Usuario;

public class UsuarioDto {
	
	public UsuarioDto() {}
	
	public UsuarioDto(Usuario usuario) {
		this.id = usuario.getId();
		this.nome = usuario.getNome();
	}
	
	public UsuarioDto(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	private Long id;	
	private String nome;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioDto usuario = (UsuarioDto) o;
        return Objects.equals(id, usuario.id) 
        		&& Objects.equals(nome, usuario.nome);
	}

	public static List<UsuarioDto> converter(List<Usuario> usuarios) {
		return usuarios.stream().map(usuario -> new UsuarioDto(usuario.getId(), usuario.getNome())).collect(Collectors.toList());
	}

}

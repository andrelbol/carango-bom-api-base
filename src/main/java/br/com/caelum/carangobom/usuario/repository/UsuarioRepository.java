package br.com.caelum.carangobom.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caelum.carangobom.usuario.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}

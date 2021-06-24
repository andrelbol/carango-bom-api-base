package br.com.caelum.carangobom.marca.repository;

import br.com.caelum.carangobom.marca.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarcaRepository extends JpaRepository<Marca, Long> {

    List<Marca> findByOrderByNome();
}

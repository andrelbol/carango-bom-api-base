package br.com.caelum.carangobom.veiculo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caelum.carangobom.veiculo.model.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
	
	List<Veiculo> findByOrderByModelo();

}

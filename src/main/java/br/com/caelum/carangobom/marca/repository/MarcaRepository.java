package br.com.caelum.carangobom.marca.repository;

import br.com.caelum.carangobom.marca.model.DashboardMarcaProjecao;
import br.com.caelum.carangobom.marca.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarcaRepository extends JpaRepository<Marca, Long> {

    List<Marca> findByOrderByNome();

    @Query(value = "SELECT m as marca,COUNT(v) as quantidadeVeiculos, SUM(v.valor) as valorTotalVeiculos\n" +
            "FROM Veiculo v\n" +
            "RIGHT JOIN Marca m\n" +
            "ON v.marca = m\n" +
            "GROUP BY m")
    List<DashboardMarcaProjecao> getSumarioMarcas();


}

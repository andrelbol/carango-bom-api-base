package br.com.caelum.carangobom.marca.repository;

import br.com.caelum.carangobom.marca.model.DashboardMarcaProjecao;
import br.com.caelum.carangobom.marca.model.Marca;
import br.com.caelum.carangobom.veiculo.model.Veiculo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MarcaRepositoryTest {

    @Autowired
    MarcaRepository marcaRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void deveriaCarregarAProjecaoDeMarcasComVeiculos(){

        Marca m1 = new Marca("Honda");
        Marca m2 = new Marca("Hyunday");
        Marca m3 = new Marca("Fiat");

        Veiculo v1 = new Veiculo(m1,2020,"Fit",new BigDecimal("50000"));
        Veiculo v2 = new Veiculo(m1,2021,"HRV",new BigDecimal("60000"));
        Veiculo v3 = new Veiculo(m2,2020,"Hyunday X",new BigDecimal("40000"));

        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.persist(v1);
        em.persist(v2);
        em.persist(v3);

        List<DashboardMarcaProjecao> sumarioMarcas = marcaRepository.getSumarioMarcas();
        assertEquals(3,sumarioMarcas.size());
        assertEquals(m1,sumarioMarcas.get(0).getMarca());
        assertEquals(2,sumarioMarcas.get(0).getQuantidadeVeiculos());
        assertEquals(new BigDecimal("110000.00"),sumarioMarcas.get(0).getValorTotalVeiculos());
        assertEquals(m2,sumarioMarcas.get(1).getMarca());
        assertEquals(1,sumarioMarcas.get(1).getQuantidadeVeiculos());
        assertEquals(new BigDecimal("40000.00"),sumarioMarcas.get(1).getValorTotalVeiculos());
        assertEquals(m3,sumarioMarcas.get(2).getMarca());
        assertEquals(0,sumarioMarcas.get(2).getQuantidadeVeiculos());
        assertNull(sumarioMarcas.get(2).getValorTotalVeiculos());

    }

}
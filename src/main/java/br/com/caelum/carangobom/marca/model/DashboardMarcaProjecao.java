package br.com.caelum.carangobom.marca.model;

import java.math.BigDecimal;

public interface DashboardMarcaProjecao {

    Marca getMarca();
    Integer getQuantidadeVeiculos();
    BigDecimal getValorTotalVeiculos();

}

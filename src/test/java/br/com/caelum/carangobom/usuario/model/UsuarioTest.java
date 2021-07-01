package br.com.caelum.carangobom.usuario.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UsuarioTest {


    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    public void setup() {
        usuario2 = new Usuario(1L, "Teste", "teste@teste.com");
        usuario1 = new Usuario(1L, "Teste", "teste@teste.com");
    }

    @Test
    void deveGerarHashcodeIgualParaDoisUsuariosComOsMesmosDados() {
        assertEquals(usuario2.hashCode(), usuario1.hashCode());
    }

    @Test
    void deveConsiderarIgualQuandoUsuarioForComparadoComEleMesmo() {
        var usuarioIgual = usuario2;
        assertEquals(usuarioIgual, usuario2);
    }

    @Test
    void naoDeveSerIgualQuandoComparadoAOutroObjeto() {
        assertNotEquals(new Object(), usuario2);
    }

    @Test
    void naoDeveSerIgualQuandoQualquerEntreOsUsuariosForemDiferentes() {
        Usuario diferente = new Usuario(2L, "Teste", "teste@teste.com");
        assertNotEquals(diferente, usuario2);
    }
}
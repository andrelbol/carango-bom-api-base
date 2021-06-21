package br.com.caelum.carangobom.marca.controller.validacao;

public class ErroDeParametroOutputDto {

    private String parametro;
    private String mensagem;

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}

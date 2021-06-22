package br.com.caelum.carangobom.exceptions;

import br.com.caelum.carangobom.validacao.ErroDeParametroOutputDto;
import br.com.caelum.carangobom.validacao.ListaDeErrosOutputDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

public class MethodArgumentNotValidExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ListaDeErrosOutputDto validacao(MethodArgumentNotValidException excecao) {
        List<ErroDeParametroOutputDto> l = new ArrayList<>();
        excecao.getBindingResult().getFieldErrors().forEach(e -> {
            ErroDeParametroOutputDto d = new ErroDeParametroOutputDto();
            d.setParametro(e.getField());
            d.setMensagem(e.getDefaultMessage());
            l.add(d);
        });
        ListaDeErrosOutputDto l2 = new ListaDeErrosOutputDto();
        l2.setErros(l);
        return l2;
    }
}

package br.com.caelum.carangobom.config.validacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ListaDeErrosOutputDto handle(MethodArgumentNotValidException excecao) {
        List<ErroDeParametroOutputDto> listaDeErros = new ArrayList<>();
        excecao.getBindingResult().getFieldErrors().forEach(e -> {
            ErroDeParametroOutputDto erroDeParametroDto = new ErroDeParametroOutputDto();
            erroDeParametroDto.setParametro(e.getField());
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            erroDeParametroDto.setMensagem(mensagem);
            listaDeErros.add(erroDeParametroDto);
        });
        ListaDeErrosOutputDto listaDeErrosOutputDto = new ListaDeErrosOutputDto();
        listaDeErrosOutputDto.setErros(listaDeErros);
        return listaDeErrosOutputDto;
    }
}

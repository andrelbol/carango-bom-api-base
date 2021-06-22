package br.com.caelum.carangobom.marca.controller;

import br.com.caelum.carangobom.marca.controller.dto.MarcaDto;
import br.com.caelum.carangobom.marca.controller.form.MarcaForm;
import br.com.caelum.carangobom.marca.controller.validacao.ErroDeParametroOutputDto;
import br.com.caelum.carangobom.marca.controller.validacao.ListaDeErrosOutputDto;
import br.com.caelum.carangobom.marca.model.Marca;
import br.com.caelum.carangobom.marca.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/marcas")
public class MarcaController {

    private MarcaRepository marcaRepository;

    @Autowired
    public MarcaController(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @GetMapping
    @Transactional
    public List<MarcaDto> lista() {
        return MarcaDto.converter(marcaRepository.findByOrderByNome());
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<MarcaDto> id(@PathVariable Long id) {
        Optional<Marca> marcaEncontrada = marcaRepository.findById(id);
        return marcaEncontrada.map(marca -> ResponseEntity.ok(new MarcaDto(marca))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<MarcaDto> cadastra(@RequestBody MarcaForm marcaForm, UriComponentsBuilder uriBuilder) {
        Marca marcaCadastrada = marcaRepository.save(marcaForm.converter());
        URI h = uriBuilder.path("/marcas/{id}").buildAndExpand(marcaCadastrada.getId()).toUri();
        return ResponseEntity.created(h).body(new MarcaDto(marcaCadastrada));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<MarcaDto> altera(@PathVariable Long id, @RequestBody MarcaForm marcaForm) {
        Optional<Marca> marcaAtual = marcaRepository.findById(id);
        if (marcaAtual.isPresent()) {
            Marca marcaAlterada = marcaForm.atualizar(id, marcaRepository);
            return ResponseEntity.ok(new MarcaDto(marcaAlterada));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Marca> deleta(@PathVariable Long id) {
        Optional<Marca> m1 = marcaRepository.findById(id);
        if (m1.isPresent()) {
            marcaRepository.deleteById(id);
            return ResponseEntity.ok(m1.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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
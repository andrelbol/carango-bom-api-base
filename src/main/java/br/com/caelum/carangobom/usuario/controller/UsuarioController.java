package br.com.caelum.carangobom.usuario.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.caelum.carangobom.usuario.controller.dto.UsuarioDto;
import br.com.caelum.carangobom.usuario.controller.form.AlterarSenhaForm;
import br.com.caelum.carangobom.usuario.controller.form.UsuarioForm;
import br.com.caelum.carangobom.usuario.model.Usuario;
import br.com.caelum.carangobom.usuario.repository.UsuarioRepository;
import br.com.caelum.carangobom.usuario.service.CriptografiaService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	UsuarioRepository usuarioRepository;
	
	@Autowired
	public UsuarioController(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	@GetMapping
	public List<UsuarioDto> listar(){
		return UsuarioDto.converter(usuarioRepository.findAll());
		
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> buscarPorId(@PathVariable Long id) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(id);
        return usuarioEncontrado.map(usuario -> ResponseEntity.ok(new UsuarioDto(usuario))).orElseGet(() -> ResponseEntity.notFound().build());
    }
	
	@PostMapping
    @Transactional
    public ResponseEntity<UsuarioDto> cadastrar(@Valid @RequestBody UsuarioForm usuarioForm, UriComponentsBuilder uriBuilder) {
        Usuario usuarioCadastrado = usuarioRepository.save(usuarioForm.converter());
        URI h = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuarioCadastrado.getId()).toUri();
        return ResponseEntity.created(h).body(new UsuarioDto(usuarioCadastrado));
    }
	
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<UsuarioDto> alterar(@PathVariable Long id, @Valid @RequestBody UsuarioForm usuarioForm) {
        Optional<Usuario> usuarioAtual = usuarioRepository.findById(id);
        if (usuarioAtual.isPresent()) {
            Usuario usuarioAlterado = usuarioForm.atualizar(usuarioAtual.get());
            return ResponseEntity.ok(new UsuarioDto(usuarioAlterado));
        }             
        
        return ResponseEntity.notFound().build();
        
    }
    
    @PutMapping("/alterar-senha/{id}")
    @Transactional
    public ResponseEntity<UsuarioDto> alterarSenha(@PathVariable Long id, @Valid @RequestBody AlterarSenhaForm alterarSenhaForm) {
        Optional<Usuario> usuarioAtual = usuarioRepository.findById(id);
        
        if (usuarioAtual.isEmpty()) {
        	return ResponseEntity.notFound().build();
        }   
        
        if(!CriptografiaService.compararSenhas(alterarSenhaForm.getSenhaAnterior(), usuarioAtual.get().getSenha())) {
        	return ResponseEntity.badRequest().build();
    	}
    	
        Usuario usuarioAlterado = alterarSenhaForm.atualizarSenha(usuarioAtual.get());
    	return ResponseEntity.ok(new UsuarioDto(usuarioAlterado));   	
        
    }
	
	@DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Usuario> deletar(@PathVariable Long id) {
        Optional<Usuario> m1 = usuarioRepository.findById(id);
    
        if (m1.isPresent()) {
        	usuarioRepository.deleteById(id);
        	 return ResponseEntity.ok().build();
        } 
        
        return ResponseEntity.notFound().build();
    }
}

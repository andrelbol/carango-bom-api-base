package br.com.caelum.carangobom.autenticacao.controller;

import br.com.caelum.carangobom.autenticacao.controller.dto.TokenDto;
import br.com.caelum.carangobom.autenticacao.controller.form.LoginForm;
import br.com.caelum.carangobom.config.security.TokenService;
import br.com.caelum.carangobom.usuario.controller.dto.UsuarioDto;
import br.com.caelum.carangobom.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form) {

        UsernamePasswordAuthenticationToken dadosLogin = form.converter();

        try {
            Authentication authentication = authenticationManager.authenticate(dadosLogin);
            String token = tokenService.gerarToken(authentication);
            return ResponseEntity.ok(new TokenDto(token, "Bearer"));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<UsuarioDto> verificarUsuarioAutenticado(@RequestHeader("Authorization") String authorization) {
        String token = tokenService.extrairToken(authorization);
        try {
            Long idUsuario = tokenService.getIdUsuario(token);
            return usuarioRepository.findById(idUsuario).map((usuario) -> {
                UsuarioDto usuarioDto = new UsuarioDto(usuario);
                return ResponseEntity.ok(usuarioDto);
            }).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

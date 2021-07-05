package br.com.caelum.carangobom.config.security;

import br.com.caelum.carangobom.usuario.model.Usuario;
import br.com.caelum.carangobom.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByNome(username);
        return usuario
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("Dados Inválidos");
                });
    }
}

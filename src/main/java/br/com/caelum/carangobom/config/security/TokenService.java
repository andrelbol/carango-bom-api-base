package br.com.caelum.carangobom.config.security;

import br.com.caelum.carangobom.usuario.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${caranbo-bom.jwt.expiration}")
    private String expiracao;

    @Value("${carango-bom.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authentication) {
        Usuario logado = (Usuario) authentication.getPrincipal();
        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiracao));
        return Jwts.builder()
                .setIssuer("API da Carango Bom")
                .setSubject(logado.getId().toString())
                .setIssuedAt(hoje)
                .setExpiration(dataExpiracao)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isTokenValido(String token) {
        try {
            getClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected Jws<Claims> getClaimsJws(String token) {
        return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
    }

    public Long getIdUsuario(String token) {
        Claims body = getClaimsJws(token).getBody();
        return Long.parseLong(body.getSubject());
    }
}

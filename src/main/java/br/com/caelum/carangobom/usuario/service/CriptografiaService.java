package br.com.caelum.carangobom.usuario.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CriptografiaService {

	public static String criptografarSenha(String senha) {
		return new BCryptPasswordEncoder().encode(senha);
	}
	
	public static boolean compararSenhas(String senha, String senhaCriptografada) {
		return new BCryptPasswordEncoder().matches(senha, senhaCriptografada);
	}
}

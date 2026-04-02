package com.proyecto.auth.services;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.proyecto.auth.entities.Usuario;
import com.proyecto.auth.repositories.UsuarioRepository;
import com.proyecto.commons.enums.EstadoRegistro;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CustomUserDetails implements UserDetailsService {
	private final UsuarioRepository usuarioRepository;
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Auth Server intentando loguear al usuario: {}", username);

        Usuario usuario = usuarioRepository.findByUsernameAndEstadoRegistro(username, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado o inactivo: " + username));

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(usuario.getRol().name()))
        );
    }
}

package com.proyecto.usuarios.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.proyecto.commons.enums.EstadoRegistro;
import com.proyecto.commons.enums.Rol;
import com.proyecto.usuarios.entities.Usuario;
import com.proyecto.usuarios.repositories.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class InitDataConfig {

	@Bean 
	CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			boolean existeAdmin = usuarioRepository.existsByUsername("admin");
			if (!existeAdmin) {
				log.info("Creando el objeto Usuario ADMIN...");
				Usuario admin = Usuario.builder()
						.username("admin")
						.password(passwordEncoder.encode("admin1234")) 
						.rol(Rol.ADMIN)
						.estadoRegistro(EstadoRegistro.ACTIVO)
						.build();
				usuarioRepository.save(admin);
				log.info("¡ÉXITO! Usuario ADMIN creado correctamente.");
			} else {
				log.info("El usuario ya existía, se omitió la creación.");
			}
		};
	}
}

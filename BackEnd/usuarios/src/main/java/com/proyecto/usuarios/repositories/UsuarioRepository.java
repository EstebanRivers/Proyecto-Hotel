package com.proyecto.usuarios.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.commons.enums.EstadoRegistro;
import com.proyecto.usuarios.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	List<Usuario> findByEstadoRegistro(EstadoRegistro estadoRegistro);
	
	Optional<Usuario> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

	boolean existsByUsernameAndEstadoRegistro(String username, EstadoRegistro estadoRegistro);
	
	boolean existsByUsername(String username);
}

package com.proyecto.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.auth.entities.Usuario;
import com.proyecto.auth.enums.EstadoRegistro;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByUsernameAndEstadoRegistro(String username, EstadoRegistro estadoRegistro);
}

package com.proyecto.huespedes.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.commons.enums.EstadoRegistro;
import com.proyecto.huespedes.entities.Huesped;

@Repository
public interface HuespedRepository extends JpaRepository<Huesped, Long>{
	
	List<Huesped> findByEstadoRegistro(EstadoRegistro estadoRegistro);
	
	boolean existsByEmailAndEstadoRegistro(String email, EstadoRegistro estadoRegistro);
	
	boolean existsByTelefonoAndEstadoRegistro(String telefono, EstadoRegistro estadoRegistro);
	
	boolean existsByDocumentoAndEstadoRegistro(String documento, EstadoRegistro estadoRegistro);

	Optional<Huesped> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
}

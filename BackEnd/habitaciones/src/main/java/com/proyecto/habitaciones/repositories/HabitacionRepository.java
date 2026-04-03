package com.proyecto.habitaciones.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.commons.enums.EstadoRegistro;
import com.proyecto.habitaciones.entities.Habitacion;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long>{
	
	List<Habitacion> findByEstadoRegistro(EstadoRegistro estadoRegistro);
	
	Optional<Habitacion> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
	
    boolean existsByNumeroAndEstadoRegistro(Integer numero, EstadoRegistro estadoRegistro);
    boolean existsByNumeroAndIdNotAndEstadoRegistro(Integer numero, Long id, EstadoRegistro estadoRegistro);
}

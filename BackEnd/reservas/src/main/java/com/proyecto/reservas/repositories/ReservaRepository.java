package com.proyecto.reservas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.commons.enums.EstadoRegistro;
import com.proyecto.reservas.entities.Reserva;
import com.proyecto.reservas.enums.EstadoReserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long>{

	List<Reserva> findByEstadoRegistro(EstadoRegistro estadoRegistro);
	
	Optional<Reserva> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
	
	boolean existsByIdHuespedAndEstadoReservaInAndEstadoRegistro(Long idHuesped, List<EstadoReserva> estados, EstadoRegistro estadoRegistro);
	
	boolean existsByIdHabitacionAndEstadoReservaInAndEstadoRegistro(Long idHabitacion, List<EstadoReserva> estados, EstadoRegistro estadoRegistro);
}

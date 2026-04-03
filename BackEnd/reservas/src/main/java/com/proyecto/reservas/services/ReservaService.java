package com.proyecto.reservas.services;

import com.proyecto.commons.services.CrudService;
import com.proyecto.reservas.dto.ReservaRequest;
import com.proyecto.reservas.dto.ReservaResponse;
import com.proyecto.reservas.enums.EstadoReserva;

public interface ReservaService extends CrudService<ReservaRequest, ReservaResponse>{
	
	ReservaResponse obtenerReservaPorIdSinEstado(Long id);	
	
	ReservaResponse cambiarEstado(Long id, EstadoReserva nuevoEstado);
	
	boolean huespedTieneReservas(Long idHuesped);
	
	boolean habitacionTieneReservas(Long idHabitacion);
}

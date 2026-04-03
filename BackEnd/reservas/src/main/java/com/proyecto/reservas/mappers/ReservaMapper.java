package com.proyecto.reservas.mappers;

import org.springframework.stereotype.Component;

import com.proyecto.commons.dto.DatosHabitacion;
import com.proyecto.commons.dto.DatosHuesped;
import com.proyecto.commons.dto.HabitacionResponse;
import com.proyecto.commons.dto.HuespedResponse;
import com.proyecto.commons.enums.EstadoRegistro;
import com.proyecto.commons.mappers.CommonMapper;
import com.proyecto.reservas.dto.ReservaRequest;
import com.proyecto.reservas.dto.ReservaResponse;
import com.proyecto.reservas.entities.Reserva;
import com.proyecto.reservas.enums.EstadoReserva;

@Component
public class ReservaMapper implements CommonMapper<ReservaRequest, ReservaResponse, Reserva>{

	@Override
	public Reserva requestToEntity(ReservaRequest request) {
		if(request == null) return null;
		
		return Reserva.builder()
				.idHuesped(request.idHuesped())
				.idHabitacion(request.idHabitacion())
				.fechaEntrada(request.fechaEntrada())
				.fechaSalida(request.fechaSalida())
				.estadoReserva(EstadoReserva.CONFIRMADA)
				.estadoRegistro(EstadoRegistro.ACTIVO)
				.build();
	}

	@Override
	public ReservaResponse entityToResponse(Reserva entity) {
		if(entity == null) return null;
		
		return new ReservaResponse(
				entity.getId(), 
				null, 
				null, 
				entity.getFechaEntrada(), 
				entity.getFechaSalida(), 
				entity.getEstadoReserva());
	}
	
	public ReservaResponse entityToResponse(Reserva entity, HuespedResponse huesped) {
		if(entity == null) return null;
		
		return new ReservaResponse(
				entity.getId(), 
				huespedResponseToDatosHuesped(huesped), 
				null, 
				entity.getFechaEntrada(), 
				entity.getFechaSalida(), 
				entity.getEstadoReserva());
	}
	
	public ReservaResponse entityToResponse(Reserva entity, HuespedResponse huesped, HabitacionResponse habitacion) {
		if(entity == null) return null;
		
		return new ReservaResponse(
				entity.getId(), 
				huespedResponseToDatosHuesped(huesped), 
				habitacionResponseToDatosHabitacion(habitacion), 
				entity.getFechaEntrada(), 
				entity.getFechaSalida(), 
				entity.getEstadoReserva());
	}

	@Override
	public Reserva updateEntityFromRequest(ReservaRequest request, Reserva entity) {
		if(entity == null) return null;
		
		entity.setIdHuesped(request.idHuesped());
		entity.setIdHabitacion(request.idHabitacion());
		entity.setFechaEntrada(request.fechaEntrada());
		entity.setFechaSalida(request.fechaSalida());
		
		return entity;
	}
	public Reserva updateEntityFromRequest(ReservaRequest request, Reserva entity, EstadoReserva estadoReserva) {
		if(entity == null) return null;
		
		updateEntityFromRequest(request, entity);
		entity.setEstadoReserva(estadoReserva);
		
		return entity;
	}
	
	private DatosHuesped huespedResponseToDatosHuesped(HuespedResponse huesped) {
		if (huesped == null) return null;
		
		return new DatosHuesped(
				huesped.id(),
				huesped.nombre(),
				huesped.edad(),
				huesped.nacionalidad(),
				huesped.email(),
				huesped.telefono(),
				huesped.documento());
	}
	
	private DatosHabitacion habitacionResponseToDatosHabitacion(HabitacionResponse habitacion) {
		if (habitacion == null) return null;
		
		return new DatosHabitacion(
				habitacion.id(),
				habitacion.numero(),
				habitacion.tipo(),
				habitacion.precio(),
				habitacion.capacidad());
	}

}

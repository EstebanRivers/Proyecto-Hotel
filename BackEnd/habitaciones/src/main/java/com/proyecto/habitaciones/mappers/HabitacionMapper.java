package com.proyecto.habitaciones.mappers;

import org.springframework.stereotype.Component;

import com.proyecto.commons.dto.HabitacionRequest;
import com.proyecto.commons.dto.HabitacionResponse;
import com.proyecto.commons.enums.EstadoHabitacion;
import com.proyecto.commons.enums.EstadoRegistro;
import com.proyecto.commons.mappers.CommonMapper;
import com.proyecto.habitaciones.entities.Habitacion;

@Component
public class HabitacionMapper implements CommonMapper<HabitacionRequest, HabitacionResponse, Habitacion>{

	@Override
	public Habitacion requestToEntity(HabitacionRequest request) {
		if (request == null) return null;
		
		return Habitacion.builder()
				.numero(request.numero())
				.tipo(request.tipo())
				.precio(request.precio())
				.capacidad(request.capacidad())
				.estadoHabitacion(EstadoHabitacion.DISPONIBLE)
				.estadoRegistro(EstadoRegistro.ACTIVO)
				.build();
	}

	@Override
	public HabitacionResponse entityToResponse(Habitacion entity) {
		if(entity == null) return null;
		
		return new HabitacionResponse(
				entity.getId(),
				entity.getNumero(),
				entity.getTipo(),
				entity.getCapacidad(),
				entity.getPrecio(),
				entity.getEstadoHabitacion(),
				entity.getEstadoRegistro());
	}

	@Override
	public Habitacion updateEntityFromRequest(HabitacionRequest request, Habitacion entity) {
		if (request == null || entity == null) return null;
		
		entity.setNumero(request.numero());
		entity.setTipo(request.tipo());
		entity.setCapacidad(request.capacidad());
		entity.setPrecio(request.precio());
		entity.setEstadoHabitacion(EstadoHabitacion.fromCodigo(request.idEstadoHabitacion()));
		
		return entity;
	}

}

package com.proyecto.habitaciones.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.commons.controllers.CommonController;
import com.proyecto.commons.dto.HabitacionRequest;
import com.proyecto.commons.dto.HabitacionResponse;
import com.proyecto.habitaciones.services.HabitacionService;

import jakarta.validation.constraints.Positive;

@RestController 
@Validated
public class HabitacionController extends CommonController<HabitacionRequest, HabitacionResponse, HabitacionService>{

	public HabitacionController(HabitacionService service) {
		super(service);
	}
	
	@GetMapping("/id-habitacion/{id}")
	public ResponseEntity<HabitacionResponse> obtenerHabitacionPorIdSinEstado(@PathVariable @Positive(message= "El ID debe ser positivo") Long id){
		return ResponseEntity.ok(service.obtenerHabitacionPorIdSinEstado(id));
	}
	
	@PutMapping("/interno/{idHabitacion}/estadoHabitacion/{idEstadoHabitacion}")
	public ResponseEntity<HabitacionResponse> cambiarDisponibilidad(
			@PathVariable @Positive(message= "El ID de la habitación debe ser positivo") Long idHabitacion, 
			@PathVariable @Positive(message= "El ID del estado debe ser positivo") Long idEstadoHabitacion) {
		
		return ResponseEntity.ok(service.cambiarEstadoHabitacion(idHabitacion, idEstadoHabitacion));
	}

	@PutMapping("/{idHabitacion}/estadoHabitacion/{idEstadoHabitacion}")
	public ResponseEntity<HabitacionResponse> cambiarDisponibilidadManual(
			@PathVariable @Positive(message= "El ID de la habitación debe ser positivo") Long idHabitacion, 
			@PathVariable @Positive(message= "El ID del estado debe ser positivo") Long idEstadoHabitacion) {
		
		return ResponseEntity.ok(service.cambiarEstadoHabitacionManual(idHabitacion, idEstadoHabitacion));
	}
}

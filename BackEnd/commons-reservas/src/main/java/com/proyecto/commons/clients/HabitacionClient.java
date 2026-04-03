package com.proyecto.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.proyecto.commons.configuration.FeignClientConfig;
import com.proyecto.commons.dto.HabitacionResponse;

@FeignClient(name = "habitaciones", configuration = FeignClientConfig.class)
public interface HabitacionClient {
	
	@GetMapping("/{id}")
	HabitacionResponse obtenerHabitacionPorId(@PathVariable Long id);
	
	@GetMapping("/id-habitacion/{id}")
	HabitacionResponse obtenerHabitacionPorIdSinEstado(@PathVariable Long id);

	@PutMapping("/interno/{idHabitacion}/estadoHabitacion/{idEstadoHabitacion}")
	HabitacionResponse cambiarEstadoHabitacion(@PathVariable Long idHabitacion, @PathVariable Long idEstadoHabitacion);
	
	@PutMapping("/{idHabitacion}/estadoHabitacion/{idEstadoHabitacion}")
	HabitacionResponse cambiarEstadoHabitacionManual(@PathVariable Long idHabitacion, @PathVariable Long idEstadoHabitacion);
}

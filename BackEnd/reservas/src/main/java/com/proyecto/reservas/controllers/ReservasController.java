package com.proyecto.reservas.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.commons.controllers.CommonController;
import com.proyecto.reservas.dto.ReservaRequest;
import com.proyecto.reservas.dto.ReservaResponse;
import com.proyecto.reservas.enums.EstadoReserva;
import com.proyecto.reservas.services.ReservaService;

import jakarta.validation.constraints.Positive;

@RestController 
@Validated
public class ReservasController extends CommonController<ReservaRequest, ReservaResponse, ReservaService>{

	public ReservasController(ReservaService service) {
		super(service);
	}
	
	@GetMapping("/id-reserva/{id}")
	public ResponseEntity<ReservaResponse> obtenerReservaPorIdSinEstado(@PathVariable @Positive(message= "El ID debe ser positivo") Long id){
		return ResponseEntity.ok(service.obtenerReservaPorIdSinEstado(id));
	}
	
	@PatchMapping("/{idReserva}/estado/{idEstado}")
	public ResponseEntity<ReservaResponse> cambiarEstadoReserva(
	        @PathVariable @Positive(message = "El ID de la reservacion debe ser positivo") Long idReserva, 
	        @PathVariable @Positive(message = "El ID de estado de la reservacion debe ser positivo") Long idEstado) {
	    
	    EstadoReserva nuevoEstado = EstadoReserva.fromCodigo(idEstado);
	    
	    ReservaResponse response = service.cambiarEstado(idReserva, nuevoEstado);
	    
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/habitacion/{idHabitacion}/tiene-reservas")
	public ResponseEntity<Boolean> habitacionTieneReservasActivas(@PathVariable @Positive Long idHabitacion) {
	    return ResponseEntity.ok(service.habitacionTieneReservas(idHabitacion));
	}

	@GetMapping("/huesped/{idHuesped}/tiene-reservas")
	public ResponseEntity<Boolean> huespedTieneReservasActivas(@PathVariable @Positive Long idHuesped) {
	    return ResponseEntity.ok(service.huespedTieneReservas(idHuesped));
	}
}

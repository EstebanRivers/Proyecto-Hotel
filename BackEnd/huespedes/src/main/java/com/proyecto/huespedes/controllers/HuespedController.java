package com.proyecto.huespedes.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.commons.controllers.CommonController;
import com.proyecto.commons.dto.HuespedRequest;
import com.proyecto.commons.dto.HuespedResponse;
import com.proyecto.huespedes.services.HuespedService;

import jakarta.validation.constraints.Positive;


@RestController
@Validated
public class HuespedController extends CommonController<HuespedRequest, HuespedResponse, HuespedService>{

	public HuespedController(HuespedService service) {
		super(service);
	}
	
	@GetMapping("/id-huesped/{id}")
	public ResponseEntity<HuespedResponse> obtenerHuespedPorIdSinEstado(
			@PathVariable 
			@Positive(message = "El ID debe ser positivo") Long id) {
		return ResponseEntity.ok(service.obtenerHuespedPorIdSinEstado(id));
	}
}

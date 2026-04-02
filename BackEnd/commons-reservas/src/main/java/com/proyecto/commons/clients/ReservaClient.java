package com.proyecto.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.proyecto.commons.configuration.FeignClientConfig;

@FeignClient(name = "reservas", configuration = FeignClientConfig.class)
public interface ReservaClient {
	
	@GetMapping("/habitacion/{idHabitacion}/tiene-reserva")
    boolean habitacionTieneReserva(@PathVariable Long idHabitacion);
	
	@GetMapping("/huesped/{idHuesped}/tiene-reserva")
	boolean huespedTieneReserva(@PathVariable Long idHuesped);

}

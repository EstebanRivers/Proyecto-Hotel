package com.proyecto.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.proyecto.commons.configuration.FeignClientConfig;
import com.proyecto.commons.dto.HuespedResponse;

@FeignClient(name = "huespedes", configuration = FeignClientConfig.class)
public interface HuespedClient {
	
	@GetMapping("/{id}")
	HuespedResponse obtenerHuespedPorId(@PathVariable Long id);
	
	@GetMapping("/id-huesped/{id}")
	HuespedResponse obtenerHuespedPorIdSinEstado(@PathVariable Long id);
}

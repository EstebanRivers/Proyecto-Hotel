package com.proyecto.huespedes.services;

import com.proyecto.commons.dto.HuespedRequest;
import com.proyecto.commons.dto.HuespedResponse;
import com.proyecto.commons.services.CrudService;

public interface HuespedService extends CrudService<HuespedRequest, HuespedResponse>{
	HuespedResponse obtenerHuespedPorIdSinEstado(Long id);
}

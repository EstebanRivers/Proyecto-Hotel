package com.proyecto.usuarios.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.commons.controllers.CommonController;
import com.proyecto.commons.dto.UsuarioRequest;
import com.proyecto.commons.dto.UsuarioResponse;
import com.proyecto.usuarios.services.UsuarioService;

@RestController
@Validated
public class UsuarioController extends CommonController<UsuarioRequest, UsuarioResponse, UsuarioService>{

	public UsuarioController(UsuarioService service) {
		super(service);
	}

}

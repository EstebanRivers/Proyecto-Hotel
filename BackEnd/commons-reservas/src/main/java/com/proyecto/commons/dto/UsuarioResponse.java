package com.proyecto.commons.dto;

import com.proyecto.commons.enums.EstadoRegistro;
import com.proyecto.commons.enums.Rol;

public record UsuarioResponse(
		Long id,
		String username,
		Rol rol,
		EstadoRegistro estaRegistro
) {}

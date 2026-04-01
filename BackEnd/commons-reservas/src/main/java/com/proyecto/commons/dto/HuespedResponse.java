package com.proyecto.commons.dto;

import com.proyecto.commons.enums.EstadoRegistro;

public record HuespedResponse(
		Long id,
		String nombre,
		Short edad,
		String email,
		String telefono,
		String documento,
		String nacionalidad,
		EstadoRegistro estadoRegistro) {
}
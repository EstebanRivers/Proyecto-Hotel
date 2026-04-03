package com.proyecto.commons.dto;

public record DatosHuesped(
		Long id,
		String nombre,
		Short edad,
		String nacionalidad,
		String email,
		String telefono,
		String documento
		
) {}

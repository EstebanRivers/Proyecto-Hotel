package com.proyecto.commons.dto;

import java.math.BigDecimal;

public record DatosHabitacion(
		Long id,
		Integer numero,
		String tipo,
		BigDecimal precio,
		Short capacidad
		
) {}

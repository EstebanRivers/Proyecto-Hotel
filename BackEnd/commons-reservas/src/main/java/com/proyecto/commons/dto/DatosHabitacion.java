package com.proyecto.commons.dto;

import java.math.BigDecimal;

public record DatosHabitacion(
		
		Integer numero,
		String tipo,
		BigDecimal precio,
		Integer capacidad
		
) {}

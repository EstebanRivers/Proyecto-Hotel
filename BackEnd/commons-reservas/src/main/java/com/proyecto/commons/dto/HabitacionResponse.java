package com.proyecto.commons.dto;

import java.math.BigDecimal;

import com.proyecto.commons.enums.EstadoHabitacion;
import com.proyecto.commons.enums.EstadoRegistro;

public record HabitacionResponse(
		Long id,
		Integer numero,
		String tipo,
		Short capacidad,
		BigDecimal precio,
		EstadoHabitacion estadoHabitacion,
		EstadoRegistro estadoRegistro
) {}

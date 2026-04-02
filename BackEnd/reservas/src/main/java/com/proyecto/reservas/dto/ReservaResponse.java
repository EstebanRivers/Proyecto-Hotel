package com.proyecto.reservas.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.proyecto.commons.dto.DatosHabitacion;
import com.proyecto.commons.dto.DatosHuesped;

public record ReservaResponse(
		
		Long id,
		DatosHuesped huesped,
		DatosHabitacion habitacion,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime fechaEntrada,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime fechaSalida,
		String estadoReserva

) {}

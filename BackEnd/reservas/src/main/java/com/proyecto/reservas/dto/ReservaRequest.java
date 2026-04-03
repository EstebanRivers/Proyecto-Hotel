package com.proyecto.reservas.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReservaRequest(
		
		@NotNull(message = "El id del huesped es requerido")
		@Positive(message = "El id del huesped debe ser positivo")
		Long idHuesped,
		
		@NotNull(message = "El id de la habitacion es requerida")
		@Positive(message = "El id de la habitacion debe ser positivo")
		Long idHabitacion,
		
		@NotNull(message = "La fecha de la reservacion es requerida")
		@FutureOrPresent(message = "La fecha de la reservacion debe ser futura")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime fechaEntrada,
		
		@NotNull(message = "La fecha de la reservacion es requerida")
		@FutureOrPresent(message = "La fecha de la reservacion debe ser futura")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime fechaSalida,
		
		@Positive(message = "El id del estado de la reserva debe ser positivo")
		Long idEstadoReserva
		
) {}

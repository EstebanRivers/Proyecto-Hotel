package com.proyecto.commons.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record HabitacionRequest(
		
		@NotNull (message = "El numero de habitacion es requerido")
        @Positive(message = "El numero debe ser mayor a 0")
        Integer numero,
        
        @NotBlank (message = "El tipo de la habitacion es requerido")
        @Size(min = 2, max = 50, message = "El tipo debe tener entre 2 y 50 caracteres")
        String tipo,

        @NotNull(message = "La capacidad de la habitacion es requerida")
        @Positive(message = "La capacidad tiene que ser mayor a 1")
		@Min(value = 1, message = "La capacidad tiene que ser mayor a 1")
        Integer capacidad,

        @NotNull(message = "El precio es requerido")
        @Positive(message = "El precio tiene que ser positivo")
        BigDecimal precio
		
) {}

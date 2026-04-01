package com.proyecto.commons.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record HuespedRequest(
		@NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
		String nombre,
		
		@NotBlank(message = "El apellido paterno es obligatorio")
        @Size(min = 2, max = 50, message = "El apellido paterno debe tener entre 2 y 50 caracteres")
		String apellidoPaterno,
		
		@NotBlank(message = "El apellido materno es obligatorio")
        @Size(min = 2, max = 50, message = "El apellido materno debe tener entre 2 y 50 caracteres")
		String apellidoMaterno,
		
		@NotNull(message = "La edad es obligatoria")
        @Min(value = 18, message = "El huésped debe ser mayor de 18 años")
		Short edad,
		
		@NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
		String email,
		
		@NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener exactamente 10 dígitos numéricos")
		String telefono,
		
		@NotBlank(message = "El documento es obligatorio")
		String documento,
		
		@NotBlank(message = "La nacionalidad es obligatoria")
		String nacionalidad) {

}

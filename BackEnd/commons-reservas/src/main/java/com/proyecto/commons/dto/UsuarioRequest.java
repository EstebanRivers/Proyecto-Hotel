package com.proyecto.commons.dto;

import com.proyecto.commons.enums.Rol;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
		
        @NotBlank(message = "El username es requerido")
        @Size(min = 4, max = 20, message = "El username debe tener entre 5 y 20 caracteres")
        String username,
        
        @NotBlank(message = "La contraseña es requerida")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "La contraseña debe tener mínimo 8 caracteres y contener letras y números")
        String password,
        
        @NotNull(message = "El rol es requerido")
        Rol rol
) {}

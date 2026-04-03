package com.proyecto.auth.dto;

public record ErrorResponse(
        int codigo,
        String mensaje
) { }

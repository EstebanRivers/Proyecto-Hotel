package com.proyecto.reservas.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EstadoReserva {

    CONFIRMADA(1L, "Reserva creada"),
    EN_CURSO(2L, "Check-in realizado"),
    FINALIZADA(3L, "Check-out realizado"),
	CANCELADA(4L, "Reserva cancelada");
    
    private final Long codigo;
	private final String descripcion;
	
	public static EstadoReserva fromCodigo(Long codigo) {
        for (EstadoReserva e : values()) {
        	if (e.codigo.equals(codigo)) {
				return e;
			}
        }
        throw new IllegalArgumentException("Código de reserva no válido: " + codigo);
    }
	
	public static EstadoReserva fromDescripcion(String descripcion) {
        for (EstadoReserva e : values()) {
            if (e.descripcion.equalsIgnoreCase(descripcion)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Descripción de reserva no válida: " + descripcion);
    }
}

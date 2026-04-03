package com.proyecto.reservas.entities;

import java.time.LocalDateTime;

import com.proyecto.commons.enums.EstadoRegistro;
import com.proyecto.reservas.enums.EstadoReserva;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "RESERVACIONES")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Reserva {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_RESERVACION")
    private Long id;
	
	@Column(name = "ID_HUESPED", nullable = false)
	private Long idHuesped;
	
	@Column(name = "ID_HABITACION", nullable = false)
	private Long idHabitacion;
	
	@Column(name = "FECHA_ENTRADA", nullable = false)
	private LocalDateTime fechaEntrada;
	
	@Column(name = "FECHA_SALIDA", nullable = false)
	private LocalDateTime fechaSalida;
	
    @Column(name = "ESTADO_RESERVA", nullable = false)
    @Enumerated(EnumType.STRING)
	private EstadoReserva estadoReserva;
	
    @Column(name = "ESTADO_REGISTRO", nullable = false)
    @Enumerated(EnumType.STRING)
	private EstadoRegistro estadoRegistro;
}

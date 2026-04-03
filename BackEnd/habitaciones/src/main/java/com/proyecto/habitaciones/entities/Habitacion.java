package com.proyecto.habitaciones.entities;

import java.math.BigDecimal;

import com.proyecto.commons.enums.EstadoHabitacion;
import com.proyecto.commons.enums.EstadoRegistro;

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

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table (name="HABITACIONES")
public class Habitacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_HABITACION")
	private Long id;
	
	@Column(name = "NUMERO", nullable = false)
	private Integer numero;

	@Column(name = "TIPO",  nullable = false, length = 50)
	private String tipo;

    @Column(name = "CAPACIDAD", nullable = false)
    private Short capacidad;

    @Column(name = "PRECIO", nullable = false)
    private BigDecimal precio;
    
    @Enumerated(EnumType.STRING)
	@Column(name = "ESTADO_HABITACION", nullable = false)
	private EstadoHabitacion estadoHabitacion;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO_REGISTRO", nullable = false)
	private EstadoRegistro estadoRegistro;
}

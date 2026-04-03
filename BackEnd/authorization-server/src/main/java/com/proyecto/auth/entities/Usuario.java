package com.proyecto.auth.entities;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.proyecto.auth.enums.EstadoRegistro;
import com.proyecto.auth.enums.Rol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USUARIOS")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {
	@Id
	@Column(name = "ID_USUARIO")
	private Long id;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "ROL")
	private Rol rol;

	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO_REGISTRO")
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	private EstadoRegistro estadoRegistro;
}

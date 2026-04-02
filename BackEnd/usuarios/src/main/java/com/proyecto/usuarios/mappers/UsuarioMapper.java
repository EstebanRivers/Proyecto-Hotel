package com.proyecto.usuarios.mappers;

import org.springframework.stereotype.Component;

import com.proyecto.commons.dto.UsuarioRequest;
import com.proyecto.commons.dto.UsuarioResponse;
import com.proyecto.commons.enums.EstadoRegistro;
import com.proyecto.commons.mappers.CommonMapper;
import com.proyecto.usuarios.entities.Usuario;

@Component
public class UsuarioMapper implements CommonMapper<UsuarioRequest, UsuarioResponse, Usuario>{

	@Override
	public Usuario requestToEntity(UsuarioRequest request) {
		if (request == null) return null;
		return Usuario.builder()
				.username(request.username())
				.rol(request.rol())
				.estadoRegistro(EstadoRegistro.ACTIVO)
				.build();
	}

	@Override
	public UsuarioResponse entityToResponse(Usuario entity) {
		return new UsuarioResponse(
				entity.getId(),
				entity.getUsername(),
				entity.getRol(),
				entity.getEstadoRegistro());
	}

	@Override
	public Usuario updateEntityFromRequest(UsuarioRequest request, Usuario entity) {
		if (entity == null || request == null) return null;
		
		entity.setUsername(request.username());
		return entity;
	}
}


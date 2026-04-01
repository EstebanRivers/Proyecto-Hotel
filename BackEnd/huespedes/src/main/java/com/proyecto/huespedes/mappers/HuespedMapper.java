package com.proyecto.huespedes.mappers;

import org.springframework.stereotype.Component;

import com.proyecto.commons.dto.HuespedRequest;
import com.proyecto.commons.dto.HuespedResponse;
import com.proyecto.commons.enums.EstadoRegistro;
import com.proyecto.commons.mappers.CommonMapper;
import com.proyecto.huespedes.entities.Huesped;

@Component
public class HuespedMapper implements CommonMapper<HuespedRequest, HuespedResponse, Huesped>{

	@Override
	public Huesped requestToEntity(HuespedRequest request) {
		if(request == null) return null;
		
		return Huesped.builder()
				.nombre(request.nombre())
				.apellidoPaterno(request.apellidoPaterno())
				.apellidoMaterno(request.apellidoMaterno())
				.edad(request.edad())
				.email(request.email())
				.telefono(request.telefono())
				.documento(request.documento())
				.nacionalidad(request.nacionalidad())
				.estadoRegistro(EstadoRegistro.ACTIVO)
				.build();
	}

	@Override
	public HuespedResponse entityToResponse(Huesped entity) {
		if(entity == null) return null;
		
		return new HuespedResponse(
				entity.getId(),
				String.join(" ",
						entity.getNombre(),
						entity.getApellidoPaterno(),
						entity.getApellidoMaterno()),
				entity.getEdad(),
				entity.getEmail(),
				entity.getTelefono(),
				entity.getDocumento(),
				entity.getNacionalidad(),
				entity.getEstadoRegistro());
	}

	@Override
	public Huesped updateEntityFromRequest(HuespedRequest request, Huesped entity) {
		if (entity == null || request == null) return null;
		
		entity.setNombre(request.nombre());
		entity.setApellidoPaterno(request.apellidoPaterno());
		entity.setApellidoMaterno(request.apellidoMaterno());
		entity.setEdad(request.edad());
		entity.setEmail(request.email());
		entity.setTelefono(request.telefono());
		entity.setDocumento(request.documento());
		entity.setNacionalidad(request.nacionalidad());
		return entity;
	}

}

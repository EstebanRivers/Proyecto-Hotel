package com.proyecto.huespedes.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.commons.dto.HuespedRequest;
import com.proyecto.commons.dto.HuespedResponse;
import com.proyecto.commons.enums.EstadoRegistro;
import com.proyecto.commons.exceptions.RecursoNoEncontradoException;
import com.proyecto.commons.exceptions.ReglaNegocioException;
import com.proyecto.huespedes.entities.Huesped;
import com.proyecto.huespedes.mappers.HuespedMapper;
import com.proyecto.huespedes.repositories.HuespedRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class HuespedServiceImpl implements HuespedService {
	private final HuespedRepository huespedRepository;
	private final HuespedMapper huespedMapper;
	
	@Override
	@Transactional(readOnly = true)
	public List<HuespedResponse> listar() {
		log.info("Listado de todos los huespedes activos solicitados");
		return huespedRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
				.map(huespedMapper::entityToResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public HuespedResponse obtenerPorId(Long id) {
		return huespedMapper.entityToResponse(obtenerHuespedOException(id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public HuespedResponse obtenerHuespedPorIdSinEstado(Long id) {
		log.info("Buscando Huesped sin estado con id: {}", id);
		return huespedMapper.entityToResponse(huespedRepository.findById(id).orElseThrow(() ->
			new RecursoNoEncontradoException("Huesped sin estado no encontrado con id: " + id)));
	}

	@Override
	public HuespedResponse registrar(HuespedRequest request) {
		log.info("Registrando nuevo huésped: {}", request.nombre());
		
		validarUnicidad(request, null);
		
		Huesped huesped = huespedMapper.requestToEntity(request);
		
		huespedRepository.save(huesped);
		
		log.info("Nuevo huesped registrado: {}", request.nombre());
		return huespedMapper.entityToResponse(huesped);
	}

	@Override
	public HuespedResponse actualizar(HuespedRequest request, Long id) {
		Huesped huesped = obtenerHuespedOException(id);
		log.info("Actualizando huésped con ID: {}", id);
		
		validarUnicidad(request, huesped);
		
		huespedMapper.updateEntityFromRequest(request, huesped);
		
		log.info("Huésped actualizado exitosamente con ID: {}", id);
		return huespedMapper.entityToResponse(huesped);
	}

	@Override
	public void eliminar(Long id) {
		Huesped huesped = obtenerHuespedOException(id);
		log.info("Eliminando Huesped con id: {}", id);
		
		// Validacion para eliminar si tiene reservas En_CURSO
		
		huesped.setEstadoRegistro(EstadoRegistro.ELIMINADO);
		log.info("Huesped con id {} ha sido marcado como eliminado", id);;
	}

	private Huesped obtenerHuespedOException(Long id) {
		log.info("Buscando Huesped activo con id: {}", id);
		return huespedRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(() ->
				new RecursoNoEncontradoException("Huesped activo no encontra con id: " + id));
	}
	
	private void validarUnicidad(HuespedRequest request, Huesped huesped) {
		if ((huesped == null || !huesped.getEmail().equalsIgnoreCase(request.email())) 
				&& huespedRepository.existsByEmailAndEstadoRegistro(request.email(), EstadoRegistro.ACTIVO)) {
			throw new ReglaNegocioException("Ya existe un huésped ACTIVO con el email: " + request.email());
		}
				
		if ((huesped == null || !huesped.getTelefono().equals(request.telefono())) 
				&& huespedRepository.existsByTelefonoAndEstadoRegistro(request.telefono(), EstadoRegistro.ACTIVO)) {
			throw new ReglaNegocioException("Ya existe un huésped ACTIVO con el teléfono: " + request.telefono());
		}

		if ((huesped == null || !huesped.getDocumento().equalsIgnoreCase(request.documento())) 
				&& huespedRepository.existsByDocumentoAndEstadoRegistro(request.documento(), EstadoRegistro.ACTIVO)) {
			throw new ReglaNegocioException("Ya existe un huésped ACTIVO con el documento: " + request.documento());
		}
	}
}

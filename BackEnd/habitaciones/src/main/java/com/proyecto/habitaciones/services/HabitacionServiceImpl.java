package com.proyecto.habitaciones.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.commons.clients.ReservaClient;
import com.proyecto.commons.dto.HabitacionRequest;
import com.proyecto.commons.dto.HabitacionResponse;
import com.proyecto.commons.enums.EstadoHabitacion;
import com.proyecto.commons.enums.EstadoRegistro;
import com.proyecto.commons.exceptions.EntidadRelacionadaException;
import com.proyecto.commons.exceptions.RecursoNoEncontradoException;
import com.proyecto.habitaciones.entities.Habitacion;
import com.proyecto.habitaciones.mappers.HabitacionMapper;
import com.proyecto.habitaciones.repositories.HabitacionRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class HabitacionServiceImpl implements HabitacionService{
	
	private final HabitacionRepository habitacionRepository;
	private final HabitacionMapper habitacionMapper;
	private final ReservaClient reservaClient;

	@Override
	public List<HabitacionResponse> listar() {
		log.info("Listado de todas las habitaciones activas");
		return habitacionRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
				.map(habitacionMapper::entityToResponse).toList();
	}

	@Override
	public HabitacionResponse obtenerPorId(Long id) {
		return habitacionMapper.entityToResponse(obtenerHabitacionOException(id));
	}

	@Override
	public HabitacionResponse registrar(HabitacionRequest request) {
		
		validarNumeroUnico(request.numero());
		
		Habitacion habitacion = habitacionMapper.requestToEntity(request);
		
		habitacion.setEstadoHabitacion(EstadoHabitacion.DISPONIBLE);
		
		habitacionRepository.save(habitacion);
		
		return habitacionMapper.entityToResponse(habitacion);
	}

	@Override
	public HabitacionResponse actualizar(HabitacionRequest request, Long id) {
		Habitacion habitacion = obtenerHabitacionOException(id);
		
		validarCambiosUnicos(request, id);
		
		habitacionMapper.updateEntityFromRequest(request, habitacion);
		
		return habitacionMapper.entityToResponse(habitacion);
	}

	@Override
	public void eliminar(Long id) {
		Habitacion habitacion = obtenerHabitacionOException(id);
		
		if (reservaClient.habitacionTieneReservas(id)) {
	        throw new EntidadRelacionadaException("No se puede eliminar la habitacion: la habitacion esta OCUPADA.");
	    }
		
		habitacion.setEstadoRegistro(EstadoRegistro.ELIMINADO);
		
	}

	@Override
	public HabitacionResponse obtenerHabitacionPorIdSinEstado(Long id) {
		return habitacionMapper.entityToResponse(habitacionRepository.findById(id).orElseThrow(() -> 
		new RecursoNoEncontradoException("Habitacion sin estado no encontrado con el id: " + id)));
	}

	@Override
	public HabitacionResponse cambiarEstadoHabitacion(Long idHabitacion, Long idEstadoHabitacion) {
		Habitacion habitacion = obtenerHabitacionOException(idHabitacion);
        
        habitacion.setEstadoHabitacion(EstadoHabitacion.fromCodigo(idEstadoHabitacion));
        
        habitacionRepository.save(habitacion);
        
        return habitacionMapper.entityToResponse(habitacion);
	}

	@Override
	public HabitacionResponse cambiarEstadoHabitacionManual(Long idHabitacion, Long idEstadoHabitacion) {
		Habitacion habitacion = obtenerHabitacionOException(idHabitacion);
        
		if (idEstadoHabitacion == 2L) { 
            if (reservaClient.habitacionTieneReservas(idHabitacion)) {
                throw new EntidadRelacionadaException("No es posible cambiar la disponibilidad: la habitacion esta OCUPADA.");
            }
        }
		
        habitacion.setEstadoHabitacion(EstadoHabitacion.fromCodigo(idEstadoHabitacion));
        return habitacionMapper.entityToResponse(habitacionRepository.save(habitacion));
	}
	
	private Habitacion obtenerHabitacionOException(Long id) {
		log.info("Buscando Habitacion Activa con el id: {}", id);
		
		return habitacionRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(() -> 
		new RecursoNoEncontradoException("Habitacion activa no encontrada con el id: " + id));
	}
	
	private void validarNumeroUnico(Integer numero) {
        if (habitacionRepository.existsByNumeroAndEstadoRegistro(numero, EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe una habitacion en el sistema con el numero: " + numero);
        }
    }
	
	private void validarCambiosUnicos(HabitacionRequest request, Long id) {
		if (habitacionRepository.existsByNumeroAndIdNotAndEstadoRegistro(request.numero(), id, EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe una habitacion en el sistema con el numero: " + request.numero());
        }
	}

}

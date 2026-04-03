package com.proyecto.reservas.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.commons.clients.HabitacionClient;
import com.proyecto.commons.clients.HuespedClient;
import com.proyecto.commons.dto.HabitacionResponse;
import com.proyecto.commons.dto.HuespedResponse;
import com.proyecto.commons.enums.EstadoHabitacion;
import com.proyecto.commons.enums.EstadoRegistro;
import com.proyecto.commons.exceptions.RecursoNoEncontradoException;
import com.proyecto.commons.exceptions.ReglaNegocioException;
import com.proyecto.reservas.dto.ReservaRequest;
import com.proyecto.reservas.dto.ReservaResponse;
import com.proyecto.reservas.entities.Reserva;
import com.proyecto.reservas.enums.EstadoReserva;
import com.proyecto.reservas.mappers.ReservaMapper;
import com.proyecto.reservas.repositories.ReservaRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ReservaServiceImpl implements ReservaService{
	
	private final ReservaRepository reservaRepository;
	private final ReservaMapper reservaMapper;
	
	private final HabitacionClient habitacionClient;
	private final HuespedClient huespedClient;

	@Override
	@Transactional(readOnly = true)
	public List<ReservaResponse> listar() {
		
		return reservaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
				.map(reserva -> reservaMapper.entityToResponse(reserva,
						obtenerHuespedResponseSinEstado(reserva.getIdHuesped()),
						obtenerHabitacionResponseSinEstado(reserva.getIdHabitacion())
						)).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public ReservaResponse obtenerPorId(Long id) {
		Reserva reserva = obtenerReservaOException(id);
		return reservaMapper.entityToResponse(reserva,
				obtenerHuespedResponseSinEstado(reserva.getIdHuesped()),
				obtenerHabitacionResponseSinEstado(reserva.getIdHabitacion())
				);
	}

	@Override
	public ReservaResponse registrar(ReservaRequest request) {
		
		HuespedResponse huesped = obtenerHuespedResponse(request.idHuesped());
		validarReservaUnicaHuesped(request.idHuesped(), List.of(EstadoReserva.EN_CURSO, EstadoReserva.CONFIRMADA), EstadoRegistro.ACTIVO);
		
		HabitacionResponse habitacion = obtenerHabitacionResponse(request.idHabitacion());
		validarDisponibilidadHabitacion(habitacion);
		validarHabitacionSinReservasActivas(request.idHabitacion());
		
		validarFechas(request.fechaEntrada(), request.fechaSalida());
		
		Reserva reserva = reservaRepository.save(reservaMapper.requestToEntity(request));
		
		cambiarDisponibilidadHabitacion(request.idHabitacion(), EstadoHabitacion.OCUPADA);
		
		return reservaMapper.entityToResponse(reserva, huesped, habitacion);
	}

	@Override
	public ReservaResponse actualizar(ReservaRequest request, Long id) {
		Reserva reserva = obtenerReservaOException(id);
		validarEstadoReservaAlActualizar(reserva);
		validarCamposEditablesPorEstado(reserva, request);
		
		HuespedResponse huesped = obtenerHuespedResponse(request.idHuesped());
		
		HabitacionResponse habitacion = obtenerHabitacionResponse(request.idHabitacion());
		validarDisponibilidadHabitacionActualizado(habitacion, request.idHabitacion(), reserva);
		
		actualizarDisponibilidadSiCambioDeHabitacion(reserva, request.idHabitacion());
		
		EstadoReserva estadoNuevo = EstadoReserva.fromCodigo(request.idEstadoReserva());
		actualizarEstado(request.idHabitacion(), reserva, estadoNuevo);
		
		reservaMapper.updateEntityFromRequest(request, reserva, estadoNuevo);
		
		return reservaMapper.entityToResponse(reserva, huesped, habitacion);
	}

	@Override
	public void eliminar(Long id) {
		Reserva reserva = obtenerReservaOException(id);
		
		validarEstadoReservaAlEliminar(reserva);
		
		habitacionClient.cambiarEstadoHabitacion(reserva.getIdHabitacion(), EstadoHabitacion.DISPONIBLE.getCodigo());
		
		reserva.setEstadoRegistro(EstadoRegistro.ELIMINADO);
		
	}

	@Override
	public ReservaResponse obtenerReservaPorIdSinEstado(Long id) {
		Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> 
		new RecursoNoEncontradoException("Reservavcion sin estado no encontrada con ID: " + id));
		
		return reservaMapper.entityToResponse(reserva,
				obtenerHuespedResponseSinEstado(reserva.getIdHuesped()),
				obtenerHabitacionResponseSinEstado(reserva.getIdHabitacion()));
	}

	@Override
	public ReservaResponse cambiarEstado(Long id, EstadoReserva nuevoEstado) {
		Reserva reserva = obtenerReservaOException(id);
		
		validarTransicionEstado(reserva.getEstadoReserva(), nuevoEstado);
		
		actualizarDisponibilidadHabitacionPorEstado(reserva.getIdHabitacion(), nuevoEstado);
		
		reserva.setEstadoReserva(nuevoEstado);
		reserva = reservaRepository.save(reserva);
		
		return reservaMapper.entityToResponse(reserva,
				obtenerHuespedResponseSinEstado(reserva.getIdHuesped()),
				obtenerHabitacionResponseSinEstado(reserva.getIdHabitacion()));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean huespedTieneReservas(Long idHuesped) {
		List<EstadoReserva> estadosActivos = List.of(EstadoReserva.EN_CURSO);
		
		return reservaRepository.existsByIdHuespedAndEstadoReservaInAndEstadoRegistro(idHuesped, estadosActivos, EstadoRegistro.ACTIVO);
	
	}

	@Override
	@Transactional(readOnly = true)
	public boolean habitacionTieneReservas(Long idHabitacion) {
		List<EstadoReserva> estadosActivos = List.of(EstadoReserva.EN_CURSO, EstadoReserva.CONFIRMADA);
		
		return reservaRepository.existsByIdHabitacionAndEstadoReservaInAndEstadoRegistro(idHabitacion, estadosActivos, EstadoRegistro.ACTIVO);
	}
	
	
	private HuespedResponse obtenerHuespedResponse(Long idHuesped) {
		return huespedClient.obtenerHuespedPorId(idHuesped);
	}
	
	private HuespedResponse obtenerHuespedResponseSinEstado(Long idHuesped) {
		return huespedClient.obtenerHuespedPorIdSinEstado(idHuesped);
	}
	
	private HabitacionResponse obtenerHabitacionResponse(Long idHabitacion) {
		return habitacionClient.obtenerHabitacionPorId(idHabitacion);
	}
	
	private HabitacionResponse obtenerHabitacionResponseSinEstado(Long idHabitacion) {
		return habitacionClient.obtenerHabitacionPorIdSinEstado(idHabitacion);
	}
	
	private Reserva obtenerReservaOException(Long id) {
		return reservaRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(()->
		new RecursoNoEncontradoException("Reservacion no encontrada con el id: " + id));
	}
	
	private void validarReservaUnicaHuesped(Long idHuesped, List<EstadoReserva> estados, EstadoRegistro estadoRegistro) {
		if(reservaRepository.existsByIdHuespedAndEstadoReservaInAndEstadoRegistro(idHuesped, estados, estadoRegistro)) {
			throw new IllegalStateException("El Huesped no puede tener más de una reserva activa");
		}
	}
	
	private void validarDisponibilidadHabitacion(HabitacionResponse habitacion) {
	    if (habitacion.estadoHabitacion() != EstadoHabitacion.DISPONIBLE) {
	        throw new IllegalStateException("No se puede elegir esta habitacion ya que no esta disponible");
	    }
	}
	
	private void validarDisponibilidadHabitacionActualizado(HabitacionResponse habitacion, Long idHabitacion, Reserva reserva) {
		if (!reserva.getIdHabitacion().equals(idHabitacion)) {
		    validarDisponibilidadHabitacion(habitacion);
		    validarHabitacionSinReservasActivas(idHabitacion);
		}
	}
	
	private void validarHabitacionSinReservasActivas(Long idHabitacion) {
	    List<EstadoReserva> estadosActivos = List.of(EstadoReserva.EN_CURSO, EstadoReserva.CONFIRMADA);
	    
	    if(reservaRepository.existsByIdHabitacionAndEstadoReservaInAndEstadoRegistro(idHabitacion, estadosActivos, EstadoRegistro.ACTIVO)) {
	        throw new IllegalStateException("La habitacion seleccionada ya tiene una reservacion activa en el sistema. Error en la disponibilidad");
	    }
	}
	
	private void validarCamposEditablesPorEstado(Reserva reserva, ReservaRequest request) {
	    if (reserva.getEstadoReserva() == EstadoReserva.CONFIRMADA) {
	        if (!reserva.getIdHuesped().equals(request.idHuesped()) ||
	            !reserva.getIdHabitacion().equals(request.idHabitacion())) {
	            throw new ReglaNegocioException("Para una reserva CONFIRMADA, solo está permitido modificar las fechas de entrada y salida.");
	        }
	    } else if (reserva.getEstadoReserva() == EstadoReserva.EN_CURSO) {
	        if (!reserva.getIdHuesped().equals(request.idHuesped()) ||
	            !reserva.getIdHabitacion().equals(request.idHabitacion()) ||
	            !reserva.getFechaEntrada().isEqual(request.fechaEntrada())) { 
	            throw new ReglaNegocioException("Para una reserva EN_CURSO, solo está permitido modificar la fecha de salida.");
	        }
	    }
	}
	
	private HabitacionClient cambiarDisponibilidadHabitacion (Long idHabitacion, EstadoHabitacion disponibilidad) {
		habitacionClient.cambiarEstadoHabitacion(idHabitacion, disponibilidad.getCodigo());
		return habitacionClient;
	}
	
	private void validarFechas(LocalDateTime fechaEntrada, LocalDateTime fechaSalida) {

        if (!fechaEntrada.isBefore(fechaSalida)) {
            throw new IllegalArgumentException("La fecha de entrada debe ser posterior a la fecha de salida");
        }
    }
	
	private void validarEstadoReservaAlActualizar(Reserva reserva) {
	    if (reserva.getEstadoReserva() == EstadoReserva.FINALIZADA || reserva.getEstadoReserva() == EstadoReserva.CANCELADA) {
	        throw new IllegalStateException("La Reservacion no puede actualizarse en estados de FINALIZADA o CANCELADA");
	    }
	}
	
	private void actualizarDisponibilidadSiCambioDeHabitacion(Reserva reserva, Long nuevoIdHabitacion) {
	    if (!reserva.getIdHabitacion().equals(nuevoIdHabitacion)) {
	        habitacionClient.cambiarEstadoHabitacion(reserva.getIdHabitacion(), EstadoHabitacion.DISPONIBLE.getCodigo());
	        habitacionClient.cambiarEstadoHabitacion(nuevoIdHabitacion, EstadoHabitacion.OCUPADA.getCodigo());
	    }
	}
	
	private void actualizarEstado(Long idHabitacion, Reserva reserva, EstadoReserva estadoNuevo){
		if (reserva.getEstadoReserva() != estadoNuevo) {
	        validarTransicionEstado(reserva.getEstadoReserva(), estadoNuevo);
	        
	        actualizarDisponibilidadHabitacionPorEstado(idHabitacion, estadoNuevo);
	    }
	}
	
	private void validarTransicionEstado(EstadoReserva estadoActual, EstadoReserva nuevoEstado) {
	    if (!esTransicionValida(estadoActual, nuevoEstado)) {
	        throw new IllegalStateException("No se puede cambiar de " + estadoActual + " a " + nuevoEstado);
	    }
	}
	
	private boolean esTransicionValida(EstadoReserva actual, EstadoReserva nuevo) {
	    switch (actual) {
	        case CONFIRMADA:
	            return nuevo == EstadoReserva.EN_CURSO || nuevo == EstadoReserva.CANCELADA;
	        case EN_CURSO:
	        	return nuevo == EstadoReserva.FINALIZADA;
	        case CANCELADA:
	        case FINALIZADA:
	            return false; 
	        default:
	            return false;
	    }
	}
	
	private void actualizarDisponibilidadHabitacionPorEstado(Long idHabitacion, EstadoReserva nuevoEstado) {
	    Long codigoDisponibilidad;

	    switch (nuevoEstado) {
	        case CONFIRMADA:
	            codigoDisponibilidad = 2L; // OCUPADA
	            break;
	        case EN_CURSO:
	            codigoDisponibilidad = 2L; // OCUPADA
	            break;
	        case CANCELADA:
	        case FINALIZADA:
	            codigoDisponibilidad = 1L; // DISPONIBLE
	            break;
	        default:
	            throw new IllegalStateException("Estado de reserva no reconocido para sincronizar disponibilidad.");
	    }

	    habitacionClient.cambiarEstadoHabitacion(idHabitacion, codigoDisponibilidad);
	}
	
	private void validarEstadoReservaAlEliminar(Reserva reserva) {
		if ( reserva.getEstadoReserva() == EstadoReserva.EN_CURSO) {
			throw new IllegalStateException("No se puede eliminar una reservacion " + 
					EstadoReserva.EN_CURSO.getDescripcion());
		}
	}
}

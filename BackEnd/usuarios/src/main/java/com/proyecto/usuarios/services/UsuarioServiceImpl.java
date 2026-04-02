package com.proyecto.usuarios.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.commons.dto.UsuarioRequest;
import com.proyecto.commons.dto.UsuarioResponse;
import com.proyecto.commons.enums.EstadoRegistro;
import com.proyecto.commons.exceptions.RecursoNoEncontradoException;
import com.proyecto.commons.exceptions.ReglaNegocioException;
import com.proyecto.usuarios.entities.Usuario;
import com.proyecto.usuarios.mappers.UsuarioMapper;
import com.proyecto.usuarios.repositories.UsuarioRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
	
    @Override
    @Transactional(readOnly = true)
	public List<UsuarioResponse> listar() {
    	log.info("Listando todos los usuarios activos");
		return usuarioRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
				.map(usuarioMapper::entityToResponse).toList();
	}
	
    @Override
    @Transactional(readOnly = true)
	public UsuarioResponse obtenerPorId(Long id) {
		return usuarioMapper.entityToResponse(obtenerUsuarioOException(id));
	}
	
    @Override
	public UsuarioResponse registrar(UsuarioRequest request) {
    	log.info("Registrando nuevo usuario: {}", request.username());
    	
    	validarUnicidad(request, null);
    	
    	Usuario usuario = usuarioMapper.requestToEntity(request);
    	usuario.setPassword(passwordEncoder.encode(request.password()));
    	
    	usuarioRepository.save(usuario);
    	log.info("Usuario registrado exitosamente con ID: {}", usuario.getId());
		return usuarioMapper.entityToResponse(usuario);
	}
	
    @Override
	public UsuarioResponse actualizar(UsuarioRequest request, Long id) {
		Usuario usuario = obtenerUsuarioOException(id);
		log.info("Actualizando usuario con ID: {}", id);
		
		validarUnicidad(request, usuario);
		
		usuarioMapper.updateEntityFromRequest(request, usuario);
		usuario.setPassword(passwordEncoder.encode(request.password()));
		usuario.setRol(request.rol());
		
		usuarioRepository.save(usuario);
		
		log.info("Usuario actualizado exitosamente con ID: {}", id);
		return usuarioMapper.entityToResponse(usuario);
	}
	
    @Override
	public void eliminar(Long id) {
    	Usuario usuario = obtenerUsuarioOException(id);
		log.info("Eliminando (lógico) usuario con id: {}", id);
		
		usuario.setEstadoRegistro(EstadoRegistro.ELIMINADO);
		log.info("Usuario con id {} ha sido marcado como eliminado", id);
	}
    
    private Usuario obtenerUsuarioOException(Long id) {
		return usuarioRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(() ->
				new RecursoNoEncontradoException("Usuario activo no encontrado con id: " + id));
	}
    
    private void validarUnicidad(UsuarioRequest request, Usuario usuarioActual) {
		if ((usuarioActual == null || !usuarioActual.getUsername().equalsIgnoreCase(request.username())) 
				&& usuarioRepository.existsByUsernameAndEstadoRegistro(request.username(), EstadoRegistro.ACTIVO)) {
			throw new ReglaNegocioException("Ya existe un usuario ACTIVO con el username: " + request.username());
		}
	}
}
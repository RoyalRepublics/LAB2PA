package com.laboratorio.streams.service;

import com.laboratorio.streams.dto.ResumenEstadoDTO;
import com.laboratorio.streams.dto.TareaRequestDTO;
import com.laboratorio.streams.dto.TareaResponseDTO;
import com.laboratorio.streams.exception.ResourceNotFoundException;
import com.laboratorio.streams.model.entity.EstadoTarea;
import com.laboratorio.streams.model.entity.Tarea;
import com.laboratorio.streams.repository.TareaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TareaServiceImpl implements TareaService {

    private final TareaRepository tareaRepository;

    public TareaServiceImpl(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TareaResponseDTO> obtenerTareas(String estado, Pageable pageable) {
        Page<Tarea> tareas;
        if (estado != null && !estado.isEmpty()) {
            EstadoTarea estadoEnum = parseEstado(estado);
            tareas = tareaRepository.findByEstado(estadoEnum, pageable);
        } else {
            tareas = tareaRepository.findAll(pageable);
        }
        return tareas.map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TareaResponseDTO obtenerTareaPorId(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con el id: " + id));
        return mapToResponseDTO(tarea);
    }

    @Override
    @Transactional
    public TareaResponseDTO crearTarea(TareaRequestDTO request) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(request.getTitulo());
        tarea.setDescripcion(request.getDescripcion());
        tarea.setEstado(parseEstado(request.getEstado()));
        tarea.setPrioridad(request.getPrioridad());
        
        Tarea tareaGuardada = tareaRepository.save(tarea);
        return mapToResponseDTO(tareaGuardada);
    }

    @Override
    @Transactional
    public TareaResponseDTO actualizarTarea(Long id, TareaRequestDTO request) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con el id: " + id));
        
        tarea.setTitulo(request.getTitulo());
        tarea.setDescripcion(request.getDescripcion());
        tarea.setEstado(parseEstado(request.getEstado()));
        tarea.setPrioridad(request.getPrioridad());
        
        Tarea tareaActualizada = tareaRepository.save(tarea);
        return mapToResponseDTO(tareaActualizada);
    }

    @Override
    @Transactional
    public void eliminarTarea(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con el id: " + id));
        tareaRepository.delete(tarea);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumenEstadoDTO> obtenerResumenTareas() {
        return tareaRepository.contarTareasPorEstado();
    }

    private TareaResponseDTO mapToResponseDTO(Tarea tarea) {
        TareaResponseDTO dto = new TareaResponseDTO();
        dto.setId(tarea.getId());
        dto.setTitulo(tarea.getTitulo());
        dto.setDescripcion(tarea.getDescripcion());
        dto.setEstado(tarea.getEstado().name());
        dto.setPrioridad(tarea.getPrioridad());
        dto.setFechaCreacion(tarea.getFechaCreacion());
        return dto;
    }

    private EstadoTarea parseEstado(String estadoStr) {
        try {
            return EstadoTarea.valueOf(estadoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado no válido. Valores permitidos: PENDIENTE, EN_PROGRESO, COMPLETADA");
        }
    }
}

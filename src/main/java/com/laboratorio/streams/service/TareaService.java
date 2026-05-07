package com.laboratorio.streams.service;

import com.laboratorio.streams.dto.ResumenEstadoDTO;
import com.laboratorio.streams.dto.TareaRequestDTO;
import com.laboratorio.streams.dto.TareaResponseDTO;
import com.laboratorio.streams.model.entity.EstadoTarea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TareaService {

    Page<TareaResponseDTO> obtenerTareas(String estado, Pageable pageable);

    TareaResponseDTO obtenerTareaPorId(Long id);

    TareaResponseDTO crearTarea(TareaRequestDTO request);

    TareaResponseDTO actualizarTarea(Long id, TareaRequestDTO request);

    void eliminarTarea(Long id);

    List<ResumenEstadoDTO> obtenerResumenTareas();
}

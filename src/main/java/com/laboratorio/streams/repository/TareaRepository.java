package com.laboratorio.streams.repository;

import com.laboratorio.streams.model.entity.EstadoTarea;
import com.laboratorio.streams.model.entity.Tarea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.laboratorio.streams.dto.ResumenEstadoDTO;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    Page<Tarea> findByEstado(EstadoTarea estado, Pageable pageable);

    @Query("SELECT t.estado as estado, COUNT(t) as cantidad FROM Tarea t GROUP BY t.estado")
    List<ResumenEstadoDTO> contarTareasPorEstado();
}

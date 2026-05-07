package com.laboratorio.streams.controller;

import com.laboratorio.streams.dto.ResumenEstadoDTO;
import com.laboratorio.streams.dto.TareaRequestDTO;
import com.laboratorio.streams.dto.TareaResponseDTO;
import com.laboratorio.streams.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @GetMapping
    public ResponseEntity<Page<TareaResponseDTO>> listarTareas(
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TareaResponseDTO> tareas = tareaService.obtenerTareas(estado, pageable);
        return ResponseEntity.ok(tareas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> obtenerTareaPorId(@PathVariable Long id) {
        TareaResponseDTO tarea = tareaService.obtenerTareaPorId(id);
        return ResponseEntity.ok(tarea);
    }

    @GetMapping("/resumen")
    public ResponseEntity<List<ResumenEstadoDTO>> obtenerResumen() {
        List<ResumenEstadoDTO> resumen = tareaService.obtenerResumenTareas();
        return ResponseEntity.ok(resumen);
    }

    @PostMapping
    public ResponseEntity<TareaResponseDTO> crearTarea(@Valid @RequestBody TareaRequestDTO request) {
        TareaResponseDTO tareaCreada = tareaService.crearTarea(request);
        return new ResponseEntity<>(tareaCreada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> actualizarTarea(
            @PathVariable Long id,
            @Valid @RequestBody TareaRequestDTO request) {
        TareaResponseDTO tareaActualizada = tareaService.actualizarTarea(id, request);
        return ResponseEntity.ok(tareaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long id) {
        tareaService.eliminarTarea(id);
        return ResponseEntity.noContent().build();
    }
}

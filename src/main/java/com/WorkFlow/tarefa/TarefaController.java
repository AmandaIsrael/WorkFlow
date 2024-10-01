package com.WorkFlow.tarefa;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarefa")
@RequiredArgsConstructor
@Validated
public class TarefaController {

    private final TarefaService tarefaService;

    @GetMapping()
    public ResponseEntity<List<TarefaDTO>> findAll() {
        List<TarefaDTO> tarefaDTOList = tarefaService.findAll();
        return ResponseEntity.ok(tarefaDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaDTO> findById(@PathVariable Long id) {
        Optional<TarefaDTO> tarefaDTO = tarefaService.findById(id);
        return tarefaDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("categoria/{categoriaId}")
    public List<TarefaDTO> findByCategoriaId(@PathVariable Long categoriaId) {
        return tarefaService.findByCategoriaId(categoriaId);
    }

    @PostMapping()
    public ResponseEntity<TarefaDTO> post(@Valid @RequestBody TarefaDTO newTarefaDTO) {
        TarefaDTO tarefaDTOCreated = tarefaService.save(newTarefaDTO);
        return ResponseEntity.ok(tarefaDTOCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaDTO> put(@PathVariable Long id, @Valid @RequestBody TarefaDTO newTarefaDTO) {
        Optional<TarefaDTO> tarefaDTOUpdated = tarefaService.put(id, newTarefaDTO);
        return tarefaDTOUpdated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TarefaDTO> delete(@PathVariable Long id) {
        boolean deleted = tarefaService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}

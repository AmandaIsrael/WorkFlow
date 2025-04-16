package com.WorkFlow.Task;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;

    @GetMapping()
    public ResponseEntity<List<TaskDTO>> findAll() {
        List<TaskDTO> taskDTOList = taskService.findAll();
        return ResponseEntity.ok(taskDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> findById(@PathVariable Long id) {
        Optional<TaskDTO> taskDTO = taskService.findById(id);
        return taskDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category_id}")
    public List<TaskDTO> findByCategoryId(@PathVariable Long categoryId) {
        return taskService.findByCategoryId(categoryId);
    }

    @PostMapping()
    public ResponseEntity<TaskDTO> post(@Valid @RequestBody TaskDTO newTaskDTO) {
        TaskDTO taskDTOCreated = taskService.save(newTaskDTO);
        return ResponseEntity.ok(taskDTOCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> put(@PathVariable Long id, @Valid @RequestBody TaskDTO newTaskDTO) {
        Optional<TaskDTO> taskDTOUpdated = taskService.put(id, newTaskDTO);
        return taskDTOUpdated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/{category_id}")
    public ResponseEntity<TaskDTO> putByCategoryId(@PathVariable Long id, @PathVariable Long category_id) {
        Optional<TaskDTO> taskDTOUpdated = taskService.putByCategoryId(id, category_id);
        return taskDTOUpdated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDTO> delete(@PathVariable Long id) {
        boolean deleted = taskService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

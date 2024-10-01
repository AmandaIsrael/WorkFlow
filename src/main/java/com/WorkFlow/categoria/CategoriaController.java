package com.WorkFlow.categoria;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categoria")
@RequiredArgsConstructor
@Validated
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping()
    public ResponseEntity<List<CategoriaDTO>> findAll() {
        List<CategoriaDTO> categoriaDTOList = categoriaService.findAll();
        return ResponseEntity.ok(categoriaDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> findById(@PathVariable Long id) {
        Optional<CategoriaDTO> categoriaDTO = categoriaService.findById(id);
        return categoriaDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<CategoriaDTO> post(@Valid @RequestBody CategoriaDTO newCategoriaDTO) {
        CategoriaDTO categoriaDTOCreated = categoriaService.save(newCategoriaDTO);
        return ResponseEntity.ok(categoriaDTOCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> put(@PathVariable Long id, @Valid @RequestBody CategoriaDTO newCategoriaDTO) {
        Optional<CategoriaDTO> categoriaDTOUpdated = categoriaService.put(id, newCategoriaDTO);
        return categoriaDTOUpdated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoriaDTO> delete(@PathVariable Long id) {
        boolean deleted = categoriaService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

package com.WorkFlow.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> categoryDTOList = categoryService.findAll();
        return ResponseEntity.ok(categoryDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        Optional<CategoryDTO> categoryDTO = categoryService.findById(id);
        return categoryDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<CategoryDTO> post(@Valid @RequestBody CategoryDTO newCategoryDTO) {
        CategoryDTO categoryDTOCreated = categoryService.save(newCategoryDTO);
        return ResponseEntity.ok(categoryDTOCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> put(@PathVariable Long id, @Valid @RequestBody CategoryDTO newCategoryDTO) {
        Optional<CategoryDTO> categoryDTOUpdated = categoryService.put(id, newCategoryDTO);
        return categoryDTOUpdated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> delete(@PathVariable Long id) {
        boolean deleted = categoryService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

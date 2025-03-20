package com.WorkFlow.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDTO> findById(Long id) {
        return categoryRepository.findById(id).map(CategoryDTO::new);
    }

    public CategoryDTO save(CategoryDTO newCategoryDTO) {
        Category newCategory = new Category(newCategoryDTO);
        Category categoryCreated = categoryRepository.save(newCategory);
        return new CategoryDTO(categoryCreated);
    }

    public Optional<CategoryDTO> put(Long id, CategoryDTO newCategoryDTO) {
        return findById(id).map(existingCategoryDTO -> {
            updatecategoryDTO(existingCategoryDTO, newCategoryDTO);
            return save(existingCategoryDTO);
        });
    }

    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }

    public boolean delete(Long id) {
        boolean exist = existsById(id);
        if (exist) {
            categoryRepository.deleteById(id);
        }
        return exist;
    }

    private void updatecategoryDTO(CategoryDTO existingCategoryDTO, CategoryDTO newCategoryDTO) {
        existingCategoryDTO.setName(newCategoryDTO.getName());
        existingCategoryDTO.setColor(newCategoryDTO.getColor());
        existingCategoryDTO.setIcon(newCategoryDTO.getIcon());
    }
}

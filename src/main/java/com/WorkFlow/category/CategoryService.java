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
                .toList();
    }

    public Optional<CategoryDTO> findById(Long id) {
        return categoryRepository.findById(id).map(CategoryDTO::new);
    }

    public CategoryDTO save(CategoryDTO newCategoryDTO) {
        if (newCategoryDTO == null) {
            throw new IllegalArgumentException("CategoryDTO can't be null");
        }

        Category newCategory = new Category(newCategoryDTO);
        Category categoryCreated = categoryRepository.save(newCategory);
        return new CategoryDTO(categoryCreated);
    }

    public Optional<CategoryDTO> put(Long id, CategoryDTO categoryDTO) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    updateCategory(existingCategory, categoryDTO);
                    return new CategoryDTO(categoryRepository.save(existingCategory));
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

    private void updateCategory(Category category, CategoryDTO dto) {
        category.setName(dto.getName());
        category.setColor(dto.getColor());
        category.setIcon(dto.getIcon());
    }
}

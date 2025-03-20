package com.WorkFlow.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryService categoryService;

    @Test
    void testIfCanFindListCategoryDTO() {
        // given
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);

        CategoryDTO categoryDTO = new CategoryDTO(category);
        categoryDTO.setId(categoryId);

        List<CategoryDTO> categoryDTOList = List.of(categoryDTO);

        List<Category> categoryList = List.of(category);

        when(categoryRepository.findAll()).thenReturn(categoryList);
        // when
        List<CategoryDTO> result = categoryService.findAll();
        // then
        verify(categoryRepository).findAll();
        assertFalse(result.isEmpty());
        assertThat(result).isEqualTo(categoryDTOList);
    }

    @Test
    void testIfCannotFindListcategoryDTO() {
        // given
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        List<CategoryDTO> result = categoryService.findAll();
        // then
        verify(categoryRepository).findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void testIfCanFindcategoryDTOWithId() {
        // given
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);

        CategoryDTO categoryDTO = new CategoryDTO(category);
        categoryDTO.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        // when
        Optional<CategoryDTO> result = categoryService.findById(categoryId);
        // then
        verify(categoryRepository).findById(categoryId);
        assertTrue(result.isPresent());
        assertThat(result.get().getId()).isEqualTo(categoryId);
    }

    @Test
    void testIfCannotFindcategoryDTOWithId() {
        // given
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        // when
        Optional<CategoryDTO> result = categoryService.findById(categoryId);
        // then
        verify(categoryRepository).findById(categoryId);
        assertFalse(result.isPresent());
    }

    @Test
    void testIfCanSavecategory() {
        // given
        Long categoryId = 1L;

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryId);
        categoryDTO.setName("nome");
        categoryDTO.setColor("#FFFFFF");
        categoryDTO.setIcon("fa-solid fa-bars");
        
        Category category = new Category(categoryDTO);
        category.setId(categoryId);
        
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        // when
        categoryService.save(categoryDTO);
        // then
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());
        
        Category capturedCategory = categoryArgumentCaptor.getValue();
        assertThat(capturedCategory).isEqualTo(category);
    }

    @Test
    void testIfCanUpdateWhenIdExists() {
        // given
        Long categoryId = 1L;

        CategoryDTO newCategoryDTO = new CategoryDTO();
        newCategoryDTO.setId(categoryId);
        newCategoryDTO.setName("nome");

        Category newCategory = new Category(newCategoryDTO);
        newCategory.setId(categoryId);

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);
        // when
        categoryService.put(categoryId, newCategoryDTO);
        // then
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());

        Category capturedCategory = categoryArgumentCaptor.getValue();
        assertThat(capturedCategory).isEqualTo(newCategory);
        assertThat(capturedCategory.getId()).isEqualTo(categoryId);
        assertThat(capturedCategory.getName()).isEqualTo("nome");
    }

    @Test
    void testIfCannotUpdateWhenIdDoesNotExists() {
        // given
        Long categoryId = 1L;

        CategoryDTO newCategoryDTO = new CategoryDTO();
        newCategoryDTO.setId(categoryId);
        newCategoryDTO.setName("nome");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        // when
        Optional<CategoryDTO> result = categoryService.put(categoryId, newCategoryDTO);
        // then
        assertFalse(result.isPresent());
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testExistsByIdWhenIdExists() {
        // given
        Long categoryId = 1L;
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        // when
        boolean result = categoryRepository.existsById(categoryId);
        // then
        verify(categoryRepository).existsById(categoryId);
        assertTrue(result);
    }

    @Test
    void testExistsByIdWhenIdDoesNotExists() {
        // given
        Long categoryId = 1L;
        when(categoryRepository.existsById(categoryId)).thenReturn(false);
        // when
        boolean result = categoryRepository.existsById(categoryId);
        // then
        verify(categoryRepository).existsById(categoryId);
        assertFalse(result);
    }

    @Test
    void testDeleteWhenIdExists() {
        // given
        Long categoryId = 1L;
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        // when
        boolean result = categoryService.delete(categoryId);
        // then
        verify(categoryRepository).existsById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
        assertTrue(result);
    }

    @Test
    void testDeleteWhenIdDoesNotExists() {
        // given
        Long categoryId = 1L;
        when(categoryRepository.existsById(categoryId)).thenReturn(false);
        // when
        boolean result = categoryService.delete(categoryId);
        // then
        verify(categoryRepository).existsById(categoryId);
        verify(categoryRepository, never()).deleteById(categoryId);
        assertFalse(result);
    }
}
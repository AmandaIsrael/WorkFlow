package com.WorkFlow.categoria;

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
class CategoriaServiceTest {

    @Mock CategoriaRepository categoriaRepository;
    @InjectMocks CategoriaService categoriaService;

    @Test
    void testIfCanFindListCategoriaDTO() {
        // given
        Long categoriaId = 1L;

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);

        CategoriaDTO categoriaDTO = new CategoriaDTO(categoria);
        categoriaDTO.setId(categoriaId);

        List<CategoriaDTO> categoriaDTOList = List.of(categoriaDTO);

        List<Categoria> categoriaList = List.of(categoria);

        when(categoriaRepository.findAll()).thenReturn(categoriaList);
        // when
        List<CategoriaDTO> result = categoriaService.findAll();
        // then
        verify(categoriaRepository).findAll();
        assertFalse(result.isEmpty());
        assertThat(result).isEqualTo(categoriaDTOList);
    }

    @Test
    void testIfCannotFindListCategoriaDTO() {
        // given
        when(categoriaRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        List<CategoriaDTO> result = categoriaService.findAll();
        // then
        verify(categoriaRepository).findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void testIfCanFindCategoriaDTOWithId() {
        // given
        Long categoriaId = 1L;

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);

        CategoriaDTO categoriaDTO = new CategoriaDTO(categoria);
        categoriaDTO.setId(categoriaId);

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));
        // when
        Optional<CategoriaDTO> result = categoriaService.findById(categoriaId);
        // then
        verify(categoriaRepository).findById(categoriaId);
        assertTrue(result.isPresent());
        assertThat(result.get().getId()).isEqualTo(categoriaId);
    }

    @Test
    void testIfCannotFindCategoriaDTOWithId() {
        // given
        Long categoriaId = 1L;
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.empty());
        // when
        Optional<CategoriaDTO> result = categoriaService.findById(categoriaId);
        // then
        verify(categoriaRepository).findById(categoriaId);
        assertFalse(result.isPresent());
    }

    @Test
    void testIfCanSaveCategoria() {
        // given
        Long categoriaId = 1L;

        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(categoriaId);
        categoriaDTO.setNome("nome");
        categoriaDTO.setCor("#FFFFFF");
        categoriaDTO.setIcone("fa-solid fa-bars");
        
        Categoria categoria = new Categoria(categoriaDTO);
        categoria.setId(categoriaId);
        
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);
        // when
        categoriaService.save(categoriaDTO);
        // then
        ArgumentCaptor<Categoria> categoriaArgumentCaptor = ArgumentCaptor.forClass(Categoria.class);
        verify(categoriaRepository).save(categoriaArgumentCaptor.capture());
        
        Categoria capturedCategoria = categoriaArgumentCaptor.getValue();
        assertThat(capturedCategoria).isEqualTo(categoria);
    }

    @Test
    void testIfCanUpdateWhenIdExists() {
        // given
        Long categoriaId = 1L;

        CategoriaDTO newCategoriaDTO = new CategoriaDTO();
        newCategoriaDTO.setId(categoriaId);
        newCategoriaDTO.setNome("nome");

        Categoria newCategoria = new Categoria(newCategoriaDTO);
        newCategoria.setId(categoriaId);

        Categoria existingCategoria = new Categoria();
        existingCategoria.setId(categoriaId);
        existingCategoria.setNome("");

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(existingCategoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(newCategoria);
        // when
        categoriaService.put(categoriaId, newCategoriaDTO);
        // then
        ArgumentCaptor<Categoria> categoriaArgumentCaptor = ArgumentCaptor.forClass(Categoria.class);
        verify(categoriaRepository).findById(categoriaId);
        verify(categoriaRepository).save(categoriaArgumentCaptor.capture());

        Categoria capturedCategoria = categoriaArgumentCaptor.getValue();
        assertThat(capturedCategoria).isEqualTo(newCategoria);
        assertThat(capturedCategoria.getId()).isEqualTo(categoriaId);
        assertThat(capturedCategoria.getNome()).isEqualTo("nome");
    }

    @Test
    void testIfCannotUpdateWhenIdDoesNotExists() {
        Long categoriaId = 1L;

        CategoriaDTO newCategoriaDTO = new CategoriaDTO();
        newCategoriaDTO.setId(categoriaId);
        newCategoriaDTO.setNome("nome");

        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.empty());
        // when
        Optional<CategoriaDTO> result = categoriaService.put(categoriaId, newCategoriaDTO);
        // then
        assertFalse(result.isPresent());
        verify(categoriaRepository).findById(categoriaId);
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void testExistsByIdWhenIdExists() {
        // given
        Long categoriaId = 1L;
        when(categoriaRepository.existsById(categoriaId)).thenReturn(true);
        // when
        boolean result = categoriaRepository.existsById(categoriaId);
        // then
        verify(categoriaRepository).existsById(categoriaId);
        assertTrue(result);
    }

    @Test
    void testExistsByIdWhenIdDoesNotExists() {
        // given
        Long categoriaId = 1L;
        when(categoriaRepository.existsById(categoriaId)).thenReturn(false);
        // when
        boolean result = categoriaRepository.existsById(categoriaId);
        // then
        verify(categoriaRepository).existsById(categoriaId);
        assertFalse(result);
    }

    @Test
    void testDeleteWhenIdExists() {
        // given
        Long categoriaId = 1L;
        when(categoriaRepository.existsById(categoriaId)).thenReturn(true);
        // when
        boolean result = categoriaService.delete(categoriaId);
        // then
        verify(categoriaRepository).existsById(categoriaId);
        verify(categoriaRepository).deleteById(categoriaId);
        assertTrue(result);
    }

    @Test
    void testDeleteWhenIdDoesNotExists() {
        // given
        Long categoriaId = 1L;
        when(categoriaRepository.existsById(categoriaId)).thenReturn(false);
        // when
        boolean result = categoriaService.delete(categoriaId);
        // then
        verify(categoriaRepository).existsById(categoriaId);
        verify(categoriaRepository, never()).deleteById(categoriaId);
        assertFalse(result);
    }
}
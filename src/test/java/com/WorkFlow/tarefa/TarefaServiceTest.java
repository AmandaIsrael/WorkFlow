package com.WorkFlow.tarefa;

import com.WorkFlow.category.Category;
import com.WorkFlow.category.CategoryDTO;
import com.WorkFlow.category.CategoryService;
import com.WorkFlow.enums.Priority;
import com.WorkFlow.enums.Status;
import com.WorkFlow.exception.CategoryNotFoundException;
import com.WorkFlow.exception.TarefaNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock private TarefaRepository tarefaRepository;
    @Mock private CategoryService categoryService;
    @InjectMocks private TarefaService tarefaService;

    @Test
    void testIfCanFindListTarefaDTO() {
        // given
        Long tarefaId = 1L;
        Tarefa tarefa = new Tarefa();
        tarefa.setId(tarefaId);

        TarefaDTO tarefaDTO = new TarefaDTO(tarefa);
        tarefaDTO.setId(tarefaId);

        List<Tarefa> tarefaList = List.of(tarefa);

        List<TarefaDTO> tarefaDTOList = List.of(tarefaDTO);

        when(tarefaRepository.findAll()).thenReturn(tarefaList);
        // when
        List<TarefaDTO> result = tarefaService.findAll();
        // then
        verify(tarefaRepository).findAll();
        assertFalse(result.isEmpty());
        assertThat(result).isEqualTo(tarefaDTOList);
    }

    @Test
    void testIfCannotFindListTarefaDTO() {
        // given
        when(tarefaRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        List<TarefaDTO> result = tarefaService.findAll();
        // then
        verify(tarefaRepository).findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void testIfCanFindTarefaDTOWithId() {
        // given
        Long tarefaId = 1L;

        Tarefa tarefa = new Tarefa();
        tarefa.setId(tarefaId);

        TarefaDTO tarefaDTO = new TarefaDTO(tarefa);
        tarefaDTO.setId(tarefaId);

        when(tarefaRepository.findById(tarefaId)).thenReturn(Optional.of(tarefa));
        // when
        Optional<TarefaDTO> result = tarefaService.findById(tarefaId);
        // then
        verify(tarefaRepository).findById(tarefaId);
        assertTrue(result.isPresent());
        assertThat(result.get().getId()).isEqualTo(tarefaId);
    }

    @Test
    void testIfCannotFindTarefaDTOWithId() {
        // given
        Long tarefaId = 1L;
        when(tarefaRepository.findById(tarefaId)).thenReturn(Optional.empty());
        // when
        Optional<TarefaDTO> result = tarefaService.findById(tarefaId);
        // then
        verify(tarefaRepository).findById(tarefaId);
        assertFalse(result.isPresent());
    }

    @Test
    void testIfCanFindListTarefaDTOWithcategoryId() {
        // given
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);

        Tarefa tarefa = new Tarefa();
        tarefa.setCategory(category);

        List<Tarefa> tarefaList = List.of(tarefa);

        TarefaDTO tarefaDTO = new TarefaDTO(tarefa);
        tarefaDTO.setCategory_id(categoryId);

        List<TarefaDTO> tarefaDTOList = List.of(tarefaDTO);

        when(tarefaRepository.findByCategoryId(categoryId)).thenReturn(tarefaList);
        // when
        List<TarefaDTO> result = tarefaService.findByCategoryId(categoryId);
        // then
        verify(tarefaRepository).findByCategoryId(categoryId);
        assertThat(tarefaDTOList).isEqualTo(result);
    }

    @Test
    void testIfCannotFindListTarefaDTOWithcategoryId() {
        // given
        Long categoryId = 1L;

        when(tarefaRepository.findByCategoryId(categoryId)).thenReturn(Collections.emptyList());
        // when
        List<TarefaDTO> result = tarefaService.findByCategoryId(categoryId);
        // then
        verify(tarefaRepository).findByCategoryId(categoryId);
        assertTrue(result.isEmpty());
    }

    @Test
    void testIfCanSaveTarefaWithoutcategoryId() {
        // given
        TarefaDTO tarefaDTO = new TarefaDTO();
        tarefaDTO.setPriority(Priority.LOW);
        tarefaDTO.setTitle("titulo");
        tarefaDTO.setStatus(Status.IN_PROGRESS);
        tarefaDTO.setDescription("descricao");
        tarefaDTO.setDeadline(LocalDate.now());

        Tarefa tarefa = new Tarefa(tarefaDTO);

        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);
        // when
        tarefaService.save(tarefaDTO);
        // then
        ArgumentCaptor<Tarefa> tarefaArgumentCaptor = ArgumentCaptor.forClass(Tarefa.class);
        verify(tarefaRepository).save(tarefaArgumentCaptor.capture());

        Tarefa capturedTarefa = tarefaArgumentCaptor.getValue();
        assertThat(capturedTarefa).isEqualTo(tarefa);
        assertNull(capturedTarefa.getCategory());
    }

    @Test
    void testIfCanSaveTarefaWithcategoryId() {
        // given
        Long categoryId = 1L;
        
        TarefaDTO tarefaDTO = new TarefaDTO();
        tarefaDTO.setCategory_id(categoryId);
        tarefaDTO.setPriority(Priority.LOW);
        tarefaDTO.setTitle("titulo");
        tarefaDTO.setStatus(Status.IN_PROGRESS);
        tarefaDTO.setDescription("descricao");
        tarefaDTO.setDeadline(LocalDate.now());

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryId);

        Category category = new Category(categoryDTO);

        Tarefa tarefa = new Tarefa(tarefaDTO);
        tarefa.setCategory(category);

        when(categoryService.findById(categoryId)).thenReturn(Optional.of(categoryDTO));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);
        // when
        tarefaService.save(tarefaDTO);
        // then
        ArgumentCaptor<Tarefa> tarefaArgumentCaptor = ArgumentCaptor.forClass(Tarefa.class);
        verify(tarefaRepository).save(tarefaArgumentCaptor.capture());

        Tarefa capturedTarefa = tarefaArgumentCaptor.getValue();
        assertThat(capturedTarefa).isEqualTo(tarefa);
        assertNotNull(capturedTarefa.getCategory());
        assertThat(capturedTarefa.getCategory().getId()).isEqualTo(categoryId);
    }

    @Test
    void testIfCannotSaveWhenCategoryDoesNotExist() {
        // given
        TarefaDTO tarefaDTO = new TarefaDTO();
        tarefaDTO.setCategory_id(1L);
        tarefaDTO.setPriority(Priority.LOW);
        tarefaDTO.setTitle("titulo");
        tarefaDTO.setStatus(Status.IN_PROGRESS);
        tarefaDTO.setDescription("descricao");
        tarefaDTO.setDeadline(LocalDate.now());

        when(categoryService.findById(1L)).thenReturn(Optional.empty());
        // then
        assertThrows(CategoryNotFoundException.class, () -> {
           // when
            tarefaService.save(tarefaDTO);
        });
    }

    @Test
    void testIfCannotTransformToDTOWhenSaving() {
        // given
        TarefaDTO tarefaDTO = new TarefaDTO();
        tarefaDTO.setPriority(Priority.LOW);
        tarefaDTO.setTitle("titulo");
        tarefaDTO.setStatus(Status.IN_PROGRESS);
        tarefaDTO.setDescription("descricao");
        tarefaDTO.setDeadline(LocalDate.now());
        // then
        assertThrows(TarefaNotFoundException.class, () -> {
            // when
            tarefaService.save(tarefaDTO);
        });
    }

    @Test
    void testIfCanUpdateWhenIdExists() {
        // given
        Long tarefaId = 1L;

        TarefaDTO newTarefaDTO = new TarefaDTO();
        newTarefaDTO.setId(tarefaId);
        newTarefaDTO.setTitle("titulo");

        Tarefa newTarefa = new Tarefa(newTarefaDTO);
        newTarefa.setId(tarefaId);

        Tarefa existingTarefa = new Tarefa();
        existingTarefa.setId(tarefaId);
        existingTarefa.setTitle("");

        when(tarefaRepository.findById(tarefaId)).thenReturn(Optional.of(existingTarefa));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(newTarefa);
        // when
        tarefaService.put(tarefaId, newTarefaDTO);
        // then
        ArgumentCaptor<Tarefa> tarefaArgumentCaptor = ArgumentCaptor.forClass(Tarefa.class);
        verify(tarefaRepository).findById(tarefaId);
        verify(tarefaRepository).save(tarefaArgumentCaptor.capture());

        Tarefa capturedTarefa = tarefaArgumentCaptor.getValue();
        assertThat(capturedTarefa).isEqualTo(newTarefa);
        assertThat(capturedTarefa.getId()).isEqualTo(tarefaId);
        assertThat(capturedTarefa.getTitle()).isEqualTo("titulo");
    }

    @Test
    void testIfCannotUpdateWhenIdDoesNotExists() {
        // given
        Long tarefaId = 1L;

        TarefaDTO newTarefaDTO = new TarefaDTO();
        newTarefaDTO.setId(tarefaId);
        newTarefaDTO.setTitle("titulo");

        when(tarefaRepository.findById(tarefaId)).thenReturn(Optional.empty());
        // when
        Optional<TarefaDTO> result = tarefaService.put(tarefaId, newTarefaDTO);
        // then
        assertFalse(result.isPresent());
        verify(tarefaRepository).findById(tarefaId);
        verify(tarefaRepository, never()).save(any(Tarefa.class));
    }

    @Test
    void testExistsByIdWhenIdExists() {
        // given
        Long tarefaId = 1L;
        when(tarefaRepository.existsById(tarefaId)).thenReturn(true);
        // when
        boolean result = tarefaService.existsById(tarefaId);
        // then
        verify(tarefaRepository).existsById(tarefaId);
        assertTrue(result);
    }

    @Test
    void testExistsByIdWhenIdDoesNotExists() {
        // given
        Long tarefaId = 1L;
        when(tarefaRepository.existsById(tarefaId)).thenReturn(false);
        // when
        boolean result = tarefaService.existsById(tarefaId);
        // then
        verify(tarefaRepository).existsById(tarefaId);
        assertFalse(result);
    }

    @Test
    void testDeleteWhenIdExists() {
        // given
        Long tarefaId = 1L;
        when(tarefaRepository.existsById(tarefaId)).thenReturn(true);
        // when
        boolean result = tarefaService.delete(tarefaId);
        // then
        verify(tarefaRepository).existsById(tarefaId);
        verify(tarefaRepository).deleteById(tarefaId);
        assertTrue(result);
    }

    @Test
    void testDeleteWhenIdDoesNotExists() {
        // given
        Long tarefaId = 1L;
        when(tarefaRepository.existsById(tarefaId)).thenReturn(false);
        // when
        boolean result = tarefaService.delete(tarefaId);
        // then
        verify(tarefaRepository).existsById(tarefaId);
        verify(tarefaRepository, never()).deleteById(tarefaId);
        assertFalse(result);
    }
}
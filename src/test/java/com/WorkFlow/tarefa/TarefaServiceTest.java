package com.WorkFlow.tarefa;

import com.WorkFlow.categoria.Categoria;
import com.WorkFlow.categoria.CategoriaDTO;
import com.WorkFlow.categoria.CategoriaService;
import com.WorkFlow.exception.CategoriaNotFoundException;
import com.WorkFlow.exception.TarefaNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock private TarefaRepository tarefaRepository;
    @Mock private CategoriaService categoriaService;
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
    void testIfCanFindListTarefaDTOWithCategoriaId() {
        // given
        Long categoriaId = 1L;

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);

        Tarefa tarefa = new Tarefa();
        tarefa.setCategoria(categoria);

        List<Tarefa> tarefaList = List.of(tarefa);

        TarefaDTO tarefaDTO = new TarefaDTO(tarefa);
        tarefaDTO.setCategoria_id(categoriaId);

        List<TarefaDTO> tarefaDTOList = List.of(tarefaDTO);

        when(tarefaRepository.findByCategoriaId(categoriaId)).thenReturn(tarefaList);
        // when
        List<TarefaDTO> result = tarefaService.findByCategoriaId(categoriaId);
        // then
        verify(tarefaRepository).findByCategoriaId(categoriaId);
        assertThat(tarefaDTOList).isEqualTo(result);
    }

    @Test
    void testIfCannotFindListTarefaDTOWithCategoriaId() {
        // given
        Long categoriaId = 1L;

        when(tarefaRepository.findByCategoriaId(categoriaId)).thenReturn(Collections.emptyList());
        // when
        List<TarefaDTO> result = tarefaService.findByCategoriaId(categoriaId);
        // then
        verify(tarefaRepository).findByCategoriaId(categoriaId);
        assertTrue(result.isEmpty());
    }

    @Test
    void testIfCanSaveTarefaWithoutCategoriaId() {
        // given
        TarefaDTO tarefaDTO = new TarefaDTO();
        tarefaDTO.setPrioridade(0);
        tarefaDTO.setTitulo("titulo");
        tarefaDTO.setStatus(0);
        tarefaDTO.setDescricao("descricao");
        tarefaDTO.setPrazo(new Date());

        Tarefa tarefa = new Tarefa(tarefaDTO);

        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);
        // when
        tarefaService.save(tarefaDTO);
        // then
        ArgumentCaptor<Tarefa> tarefaArgumentCaptor = ArgumentCaptor.forClass(Tarefa.class);
        verify(tarefaRepository).save(tarefaArgumentCaptor.capture());

        Tarefa capturedTarefa = tarefaArgumentCaptor.getValue();
        assertThat(capturedTarefa).isEqualTo(tarefa);
        assertNull(capturedTarefa.getCategoria());
    }

    @Test
    void testIfCanSaveTarefaWithCategoriaId() {
        // given
        Long categoriaId = 1L;
        
        TarefaDTO tarefaDTO = new TarefaDTO();
        tarefaDTO.setCategoria_id(categoriaId);
        tarefaDTO.setPrioridade(0);
        tarefaDTO.setTitulo("titulo");
        tarefaDTO.setStatus(0);
        tarefaDTO.setDescricao("descricao");
        tarefaDTO.setPrazo(new Date());

        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(categoriaId);

        Categoria categoria = new Categoria(categoriaDTO);

        Tarefa tarefa = new Tarefa(tarefaDTO);
        tarefa.setCategoria(categoria);

        when(categoriaService.findById(categoriaId)).thenReturn(Optional.of(categoriaDTO));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);
        // when
        tarefaService.save(tarefaDTO);
        // then
        ArgumentCaptor<Tarefa> tarefaArgumentCaptor = ArgumentCaptor.forClass(Tarefa.class);
        verify(tarefaRepository).save(tarefaArgumentCaptor.capture());

        Tarefa capturedTarefa = tarefaArgumentCaptor.getValue();
        assertThat(capturedTarefa).isEqualTo(tarefa);
        assertNotNull(capturedTarefa.getCategoria());
        assertThat(capturedTarefa.getCategoria().getId()).isEqualTo(categoriaId);
    }

    @Test
    void testIfCannotSaveWhenCategoriaDoesNotExist() {
        // given
        TarefaDTO tarefaDTO = new TarefaDTO();
        tarefaDTO.setCategoria_id(1L);
        tarefaDTO.setPrioridade(0);
        tarefaDTO.setTitulo("titulo");
        tarefaDTO.setStatus(0);
        tarefaDTO.setDescricao("descricao");
        tarefaDTO.setPrazo(new Date());

        when(categoriaService.findById(1L)).thenReturn(Optional.empty());
        // then
        assertThrows(CategoriaNotFoundException.class, () -> {
           // when
            tarefaService.save(tarefaDTO);
        });
    }

    @Test
    void testIfCannotTransformToDTOWhenSaving() {
        // given
        TarefaDTO tarefaDTO = new TarefaDTO();
        tarefaDTO.setPrioridade(0);
        tarefaDTO.setTitulo("titulo");
        tarefaDTO.setStatus(0);
        tarefaDTO.setDescricao("descricao");
        tarefaDTO.setPrazo(new Date());
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
        newTarefaDTO.setTitulo("titulo");

        Tarefa newTarefa = new Tarefa(newTarefaDTO);
        newTarefa.setId(tarefaId);

        Tarefa existingTarefa = new Tarefa();
        existingTarefa.setId(tarefaId);
        existingTarefa.setTitulo("");

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
        assertThat(capturedTarefa.getTitulo()).isEqualTo("titulo");
    }

    @Test
    void testIfCannotUpdateWhenIdDoesNotExists() {
        // given
        Long tarefaId = 1L;

        TarefaDTO newTarefaDTO = new TarefaDTO();
        newTarefaDTO.setId(tarefaId);
        newTarefaDTO.setTitulo("titulo");

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
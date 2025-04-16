package com.WorkFlow.Task;

import com.WorkFlow.category.Category;
import com.WorkFlow.category.CategoryDTO;
import com.WorkFlow.category.CategoryService;
import com.WorkFlow.enums.Priority;
import com.WorkFlow.enums.Status;
import com.WorkFlow.exception.CategoryNotFoundException;
import com.WorkFlow.exception.TaskNotFoundException;
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
class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @Mock private CategoryService categoryService;
    @InjectMocks private TaskService taskService;

    @Test
    void testIfCanFindListTaskDTO() {
        // given
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);

        TaskDTO taskDTO = new TaskDTO(task);
        taskDTO.setId(taskId);

        List<Task> taskList = List.of(task);

        List<TaskDTO> taskDTOList = List.of(taskDTO);

        when(taskRepository.findAll()).thenReturn(taskList);
        // when
        List<TaskDTO> result = taskService.findAll();
        // then
        verify(taskRepository).findAll();
        assertFalse(result.isEmpty());
        assertThat(result).isEqualTo(taskDTOList);
    }

    @Test
    void testIfCannotFindListTaskDTO() {
        // given
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        List<TaskDTO> result = taskService.findAll();
        // then
        verify(taskRepository).findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void testIfCanFindTaskDTOWithId() {
        // given
        Long taskId = 1L;

        Task task = new Task();
        task.setId(taskId);

        TaskDTO taskDTO = new TaskDTO(task);
        taskDTO.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        // when
        Optional<TaskDTO> result = taskService.findById(taskId);
        // then
        verify(taskRepository).findById(taskId);
        assertTrue(result.isPresent());
        assertThat(result.get().getId()).isEqualTo(taskId);
    }

    @Test
    void testIfCannotFindTaskDTOWithId() {
        // given
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        // when
        Optional<TaskDTO> result = taskService.findById(taskId);
        // then
        verify(taskRepository).findById(taskId);
        assertFalse(result.isPresent());
    }

    @Test
    void testIfCanFindListTaskDTOWithcategoryId() {
        // given
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);

        Task task = new Task();
        task.setCategory(category);

        List<Task> taskList = List.of(task);

        TaskDTO taskDTO = new TaskDTO(task);
        taskDTO.setCategory_id(categoryId);

        List<TaskDTO> taskDTOList = List.of(taskDTO);

        when(taskRepository.findByCategoryId(categoryId)).thenReturn(taskList);
        // when
        List<TaskDTO> result = taskService.findByCategoryId(categoryId);
        // then
        verify(taskRepository).findByCategoryId(categoryId);
        assertThat(taskDTOList).isEqualTo(result);
    }

    @Test
    void testIfCannotFindListTaskDTOWithcategoryId() {
        // given
        Long categoryId = 1L;

        when(taskRepository.findByCategoryId(categoryId)).thenReturn(Collections.emptyList());
        // when
        List<TaskDTO> result = taskService.findByCategoryId(categoryId);
        // then
        verify(taskRepository).findByCategoryId(categoryId);
        assertTrue(result.isEmpty());
    }

    @Test
    void testIfCanSaveTaskWithoutcategoryId() {
        // given
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setPriority(Priority.LOW);
        taskDTO.setTitle("titulo");
        taskDTO.setStatus(Status.IN_PROGRESS);
        taskDTO.setDescription("descricao");
        taskDTO.setDeadline(LocalDate.now());

        Task task = new Task(taskDTO);

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        // when
        taskService.save(taskDTO);
        // then
        ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(taskArgumentCaptor.capture());

        Task capturedTask = taskArgumentCaptor.getValue();
        assertThat(capturedTask).isEqualTo(task);
        assertNull(capturedTask.getCategory().getId());
    }

    @Test
    void testIfCanSaveTaskWithcategoryId() {
        // given
        Long categoryId = 1L;
        
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setCategory_id(categoryId);
        taskDTO.setPriority(Priority.LOW);
        taskDTO.setTitle("titulo");
        taskDTO.setStatus(Status.IN_PROGRESS);
        taskDTO.setDescription("descricao");
        taskDTO.setDeadline(LocalDate.now());

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryId);

        Category category = new Category(categoryDTO);

        Task task = new Task(taskDTO);
        task.setCategory(category);

        when(categoryService.findById(categoryId)).thenReturn(Optional.of(categoryDTO));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        // when
        taskService.save(taskDTO);
        // then
        ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(taskArgumentCaptor.capture());

        Task capturedTask = taskArgumentCaptor.getValue();
        assertThat(capturedTask).isEqualTo(task);
        assertNotNull(capturedTask.getCategory());
        assertThat(capturedTask.getCategory().getId()).isEqualTo(categoryId);
    }

    @Test
    void testIfCannotSaveWhenCategoryDoesNotExist() {
        // given
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setCategory_id(1L);
        taskDTO.setPriority(Priority.LOW);
        taskDTO.setTitle("titulo");
        taskDTO.setStatus(Status.IN_PROGRESS);
        taskDTO.setDescription("descricao");
        taskDTO.setDeadline(LocalDate.now());

        when(categoryService.findById(1L)).thenReturn(Optional.empty());
        // then
        assertThrows(CategoryNotFoundException.class, () -> {
           // when
            taskService.save(taskDTO);
        });
    }

    @Test
    void testIfCannotSaveWhenTaskDtoIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.save(null);
        });
    }

    @Test
    void testIfCanUpdateWhenIdExists() {
        // given
        Long taskId = 1L;

        TaskDTO newTaskDTO = new TaskDTO();
        newTaskDTO.setId(taskId);
        newTaskDTO.setTitle("titulo");

        Task newTask = new Task(newTaskDTO);
        newTask.setId(taskId);

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(newTask);
        // when
        taskService.put(taskId, newTaskDTO);
        // then
        ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(taskArgumentCaptor.capture());

        Task capturedTask = taskArgumentCaptor.getValue();
        assertThat(capturedTask).isEqualTo(newTask);
        assertThat(capturedTask.getId()).isEqualTo(taskId);
        assertThat(capturedTask.getTitle()).isEqualTo("titulo");
    }

    @Test
    void testIfCannotUpdateWhenIdDoesNotExists() {
        // given
        Long taskId = 1L;

        TaskDTO newTaskDTO = new TaskDTO();
        newTaskDTO.setId(taskId);
        newTaskDTO.setTitle("titulo");

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        // when
        Optional<TaskDTO> result = taskService.put(taskId, newTaskDTO);
        // then
        assertFalse(result.isPresent());
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void putByCategoryId_shouldUpdateTask_whenTaskAndCategoryExists() {
        // given
        Long taskId = 1L, categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);

        CategoryDTO categoryDTO = new CategoryDTO(category);
        categoryDTO.setId(categoryId);

        Task existingTask = new Task();
        existingTask.setId(taskId);

        Task newTask = new Task();
        newTask.setId(taskId);
        newTask.setCategory(category);

        TaskDTO newTaskDTO = new TaskDTO(newTask);
        newTaskDTO.setId(taskId);
        newTaskDTO.setCategory_id(categoryId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(categoryService.findById(categoryId)).thenReturn(Optional.of(categoryDTO));
        when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        // when
        taskService.putByCategoryId(taskId, categoryId);
        // then
        ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(taskArgumentCaptor.capture());

        Task capturedTask = taskArgumentCaptor.getValue();
        assertThat(capturedTask).isEqualTo(newTask);
        assertThat(capturedTask.getId()).isEqualTo(taskId);
        assertThat(capturedTask.getCategory().getId()).isEqualTo(categoryId);
    }

    @Test
    void putByCategoryId_shouldUpdateTask_whenTaskDoesNotExists() {
        // given
        Long taskId = 1L, categoryId = 1L;

        TaskDTO newTaskDTO = new TaskDTO();
        newTaskDTO.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        // when
        assertThrows(TaskNotFoundException.class, () -> taskService.putByCategoryId(taskId, categoryId));
    }

    @Test
    void putByCategoryId_shouldUpdateTask_whenCategoryDoesNotExists() {
        // given
        Long taskId = 1L, categoryId = 1L;

        Task existingTaskDTO = new Task();
        existingTaskDTO.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTaskDTO));
        when(categoryService.findById(categoryId)).thenReturn(Optional.empty());
        // when
        assertThrows(CategoryNotFoundException.class, () -> taskService.putByCategoryId(taskId, categoryId));
    }

    @Test
    void testExistsByIdWhenIdExists() {
        // given
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);
        // when
        boolean result = taskService.existsById(taskId);
        // then
        verify(taskRepository).existsById(taskId);
        assertTrue(result);
    }

    @Test
    void testExistsByIdWhenIdDoesNotExists() {
        // given
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(false);
        // when
        boolean result = taskService.existsById(taskId);
        // then
        verify(taskRepository).existsById(taskId);
        assertFalse(result);
    }

    @Test
    void testDeleteWhenIdExists() {
        // given
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);
        // when
        boolean result = taskService.delete(taskId);
        // then
        verify(taskRepository).existsById(taskId);
        verify(taskRepository).deleteById(taskId);
        assertTrue(result);
    }

    @Test
    void testDeleteWhenIdDoesNotExists() {
        // given
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(false);
        // when
        boolean result = taskService.delete(taskId);
        // then
        verify(taskRepository).existsById(taskId);
        verify(taskRepository, never()).deleteById(taskId);
        assertFalse(result);
    }
}
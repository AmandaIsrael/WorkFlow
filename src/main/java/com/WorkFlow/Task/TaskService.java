package com.WorkFlow.Task;

import com.WorkFlow.category.Category;
import com.WorkFlow.exception.CategoryNotFoundException;
import com.WorkFlow.category.CategoryService;
import com.WorkFlow.exception.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryService categoryService;

    public List<TaskDTO> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(this::toTaskDTO)
                .toList();
    }

    public Optional<TaskDTO> findById(Long id) {
        return taskRepository.findById(id).map(this::toTaskDTO);
    }

    public List<TaskDTO> findByCategoryId(Long categoryId) {
        return taskRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::toTaskDTO)
                .toList();
    }

    public TaskDTO save(TaskDTO newTaskDTO) {
        Task newTask = new Task(newTaskDTO);
        updateCategory(newTask, newTaskDTO);

        Task taskCreated = taskRepository.save(newTask);
        return toTaskDTO(taskCreated);
    }

    public Optional<TaskDTO> put(Long id, TaskDTO newTaskDTO) {
        return findById(id).map(existingTaskDTO -> {
            updateTaskDTO(existingTaskDTO, newTaskDTO);
            return save(existingTaskDTO);
        });
    }

    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }

    public boolean delete(Long id) {
        boolean exist = existsById(id);
        if (exist) {
            taskRepository.deleteById(id);
        }
        return exist;
    }

    private Category getCategory(Long id) {
        return categoryService.findById(id)
                .map(Category::new)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    private TaskDTO toTaskDTO(Task task) {
        if (task == null) {
            throw new TaskNotFoundException();
        }
        TaskDTO taskDTO = new TaskDTO(task);
        updateCategoryId(task, taskDTO);
        return taskDTO;
    }

    private void updateTaskDTO(TaskDTO existingTaskDTO, TaskDTO newTaskDTO) {
        existingTaskDTO.setStatus(newTaskDTO.getStatus());
        existingTaskDTO.setDeadline(newTaskDTO.getDeadline());
        existingTaskDTO.setDescription(newTaskDTO.getDescription());
        existingTaskDTO.setTitle(newTaskDTO.getTitle());
        existingTaskDTO.setPriority(newTaskDTO.getPriority());
        existingTaskDTO.setCategory_id(newTaskDTO.getCategory_id());
    }

    private void updateCategoryId(Task task, TaskDTO taskDTO) {
        Optional.ofNullable(task.getCategory())
                .map(Category::getId)
                .ifPresent(taskDTO::setCategory_id);
    }

    private void updateCategory(Task task, TaskDTO taskDTO) {
        Optional.ofNullable(taskDTO.getCategory_id())
                .map(this::getCategory)
                .ifPresent(task::setCategory);
    }
}

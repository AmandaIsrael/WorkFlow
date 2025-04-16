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
        if (newTaskDTO == null) {
            throw new IllegalArgumentException("TaskDTO can't be null");
        }

        Task newTask = new Task(newTaskDTO);
        setCategoryIfPresent(newTask, newTaskDTO);

        Task taskCreated = taskRepository.save(newTask);
        return toTaskDTO(taskCreated);
    }

    public Optional<TaskDTO> put(Long id, TaskDTO newTaskDTO) {
        return taskRepository.findById(id)
                .map(task -> {
                    updateTask(task, newTaskDTO);
                    setCategoryIfPresent(task, newTaskDTO);
                    return taskRepository.save(task);
                })
                .map(this::toTaskDTO);
    }

    public Optional<TaskDTO> putByCategoryId(Long id, Long category_id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setCategory(getCategory(category_id));
        Task taskCreated = taskRepository.save(task);
        return Optional.of(toTaskDTO(taskCreated));
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
        TaskDTO dto = new TaskDTO(task);
        setCategoryIdIfPresent(task, dto);
        return dto;
    }

    private void updateTask(Task task, TaskDTO dto) {
        task.setStatus(dto.getStatus());
        task.setDeadline(dto.getDeadline());
        task.setDescription(dto.getDescription());
        task.setTitle(dto.getTitle());
        task.setPriority(dto.getPriority());
    }

    private void setCategoryIdIfPresent(Task task, TaskDTO taskDTO) {
        Optional.ofNullable(task.getCategory())
                .map(Category::getId)
                .ifPresent(taskDTO::setCategory_id);
    }

    private void setCategoryIfPresent(Task task, TaskDTO taskDTO) {
        Optional.ofNullable(taskDTO.getCategory_id())
                .map(this::getCategory)
                .ifPresent(task::setCategory);
    }
}

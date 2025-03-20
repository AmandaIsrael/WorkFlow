package com.WorkFlow.category;

import com.WorkFlow.Task.Task;
import com.WorkFlow.Task.TaskDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CategoryDTO {

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.color = category.getColor();
        this.name = category.getName();
        this.icon = category.getIcon();

        for (Task task : category.getTaskList()) {
            TaskDTO taskDTO = new TaskDTO(task);
            this.taskList.add(taskDTO);
        }
    }

    private Long id;
    @NotNull(message = "The 'name' field cannot be null.")
    private String name;
    private String color;
    private String icon;

    private List<TaskDTO> taskList = new ArrayList<>();
}
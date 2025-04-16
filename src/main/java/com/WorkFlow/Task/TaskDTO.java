package com.WorkFlow.Task;

import com.WorkFlow.enums.Priority;
import com.WorkFlow.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TaskDTO {

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.status = task.getStatus();
        this.deadline = task.getDeadline();
        this.description = task.getDescription();
        this.title = task.getTitle();
        this.priority = task.getPriority();

        if (task.getCategory() != null) {
            this.category_id = task.getCategory().getId();
        }
    }

    private Long id;
    private Long category_id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate deadline;
    private String description;
    @NotNull(message = "The 'title' field cannot be null.")
    private String title;
    private Status status;
    private Priority priority;
}

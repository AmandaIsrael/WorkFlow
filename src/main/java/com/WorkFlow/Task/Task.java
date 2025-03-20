package com.WorkFlow.Task;

import com.WorkFlow.category.Category;
import com.WorkFlow.enums.Priority;
import com.WorkFlow.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Task")
public class Task {

    public Task(TaskDTO taskDTO) {
        this.id = taskDTO.getId();
        this.status = taskDTO.getStatus();
        this.deadline = taskDTO.getDeadline();
        this.description = taskDTO.getDescription();
        this.title = taskDTO.getTitle();
        this.priority = taskDTO.getPriority();
        this.category.setId(taskDTO.getCategory_id());
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate deadline;
    private String description;
    private String title;
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    @Enumerated(EnumType.ORDINAL)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category = new Category();
}

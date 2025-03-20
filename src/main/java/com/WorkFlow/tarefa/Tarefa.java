package com.WorkFlow.tarefa;

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
@Table(name = "tarefa")
public class Tarefa {

    public Tarefa(TarefaDTO tarefaDTO) {
        this.id = tarefaDTO.getId();
        this.status = tarefaDTO.getStatus();
        this.deadline = tarefaDTO.getDeadline();
        this.description = tarefaDTO.getDescription();
        this.title = tarefaDTO.getTitle();
        this.priority = tarefaDTO.getPriority();
        this.category.setId(tarefaDTO.getCategory_id());
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

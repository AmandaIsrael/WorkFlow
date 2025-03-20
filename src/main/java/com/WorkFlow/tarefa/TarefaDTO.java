package com.WorkFlow.tarefa;

import com.WorkFlow.enums.Priority;
import com.WorkFlow.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TarefaDTO {

    public TarefaDTO(Tarefa tarefa) {
        this.id = tarefa.getId();
        this.status = tarefa.getStatus();
        this.deadline = tarefa.getDeadline();
        this.description = tarefa.getDescription();
        this.title = tarefa.getTitle();
        this.priority = tarefa.getPriority();

        if (tarefa.getCategory() != null) {
            this.category_id = tarefa.getCategory().getId();
        }
    }

    private Long id;
    private Long category_id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate deadline;
    private String description;
    @NotNull(message = "The 'title' field cannot be null.")
    private String title;
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    @Enumerated(EnumType.ORDINAL)
    private Priority priority;
}

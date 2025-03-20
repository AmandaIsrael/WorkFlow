package com.WorkFlow.category;

import com.WorkFlow.tarefa.Tarefa;
import com.WorkFlow.tarefa.TarefaDTO;
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

        for (Tarefa tarefa : category.getTaskList()) {
            TarefaDTO tarefaDTO = new TarefaDTO(tarefa);
            this.taskList.add(tarefaDTO);
        }
    }

    private Long id;
    @NotNull(message = "The 'name' field cannot be null.")
    private String name;
    private String color;
    private String icon;

    private List<TarefaDTO> taskList = new ArrayList<>();
}
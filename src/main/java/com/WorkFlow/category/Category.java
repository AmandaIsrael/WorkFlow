package com.WorkFlow.category;

import com.WorkFlow.tarefa.Tarefa;
import com.WorkFlow.tarefa.TarefaDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "category")
public class Category {

    public Category(CategoryDTO categoryDTO) {
        this.id = categoryDTO.getId();
        this.name = categoryDTO.getName();
        this.color = categoryDTO.getColor();
        this.icon = categoryDTO.getIcon();

        for (TarefaDTO tarefaDTO : categoryDTO.getTaskList()) {
            Tarefa tarefa = new Tarefa(tarefaDTO);
            this.taskList.add(tarefa);
        }
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private String icon;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<Tarefa> taskList = new ArrayList<>();
}
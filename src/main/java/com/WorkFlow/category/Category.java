package com.WorkFlow.category;

import com.WorkFlow.Task.Task;
import com.WorkFlow.Task.TaskDTO;
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

        for (TaskDTO taskDTO : categoryDTO.getTaskList()) {
            Task task = new Task(taskDTO);
            this.taskList.add(task);
        }
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private String icon;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<Task> taskList = new ArrayList<>();
}
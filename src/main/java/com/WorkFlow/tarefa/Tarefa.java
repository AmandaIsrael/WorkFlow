package com.WorkFlow.tarefa;

import com.WorkFlow.categoria.Categoria;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tarefa")
public class Tarefa {

    public Tarefa(TarefaDTO tarefaDTO) {
        this.id = tarefaDTO.getId();
        this.status = tarefaDTO.getStatus();
        this.prazo = tarefaDTO.getPrazo();
        this.descricao = tarefaDTO.getDescricao();
        this.titulo = tarefaDTO.getTitulo();
        this.prioridade = tarefaDTO.getPrioridade();
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date prazo;
    private String descricao;
    private String titulo;
    private Integer status;
    private Integer prioridade;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}

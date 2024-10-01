package com.WorkFlow.tarefa;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class TarefaDTO {

    public TarefaDTO(Tarefa tarefa) {
        this.id = tarefa.getId();
        this.status = tarefa.getStatus();
        this.dataInicio = tarefa.getDataInicio();
        this.descricao = tarefa.getDescricao();
        this.titulo = tarefa.getTitulo();
        this.prioridade = tarefa.getPrioridade();
    }

    private Long id;
    private Long categoria_id;
    private Integer status;
    private Date dataInicio;
    private String descricao;
    @NotNull(message = "The 'titulo' field cannot be null.")
    private String titulo;
    private Integer prioridade;
}

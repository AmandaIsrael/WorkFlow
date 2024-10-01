package com.WorkFlow.exception;

public class TarefaNotFoundException extends RuntimeException {

    public TarefaNotFoundException(Long tarefaId) {
        super("Tarefa with id " + tarefaId + " was not found.");
    }

    public TarefaNotFoundException() {
        super("Tarefa was not found.");
    }
}

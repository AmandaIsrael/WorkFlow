package com.WorkFlow.exception;

public class CategoriaNotFoundException extends RuntimeException {

    public CategoriaNotFoundException(Long categoriaId) {
        super("Categoria with id " + categoriaId + " was not found.");
    }
}

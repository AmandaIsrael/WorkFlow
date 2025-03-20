package com.WorkFlow.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long categoryId) {
        super("Category with id " + categoryId + " was not found.");
    }
}

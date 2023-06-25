package ru.practicum.ewm.exception;

import javax.persistence.EntityNotFoundException;

public class CompilationNotFoundException extends EntityNotFoundException {

    public CompilationNotFoundException(String message) {
        super(message);
    }
}

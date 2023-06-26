package ru.practicum.ewm.exception;

import javax.persistence.EntityNotFoundException;

public class RequestNotFoundException extends EntityNotFoundException {

    public RequestNotFoundException(String message) {
        super(message);
    }
}

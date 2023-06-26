package ru.practicum.ewm.exception;

import javax.persistence.EntityNotFoundException;

public class EventNotFoundException extends EntityNotFoundException {

    public EventNotFoundException(String message) {
        super(message);
    }
}

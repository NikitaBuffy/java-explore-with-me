package ru.practicum.ewm.exception;

import javax.persistence.EntityNotFoundException;

public class CommentNotFoundException extends EntityNotFoundException {

    public CommentNotFoundException(String message) {
        super(message);
    }
}

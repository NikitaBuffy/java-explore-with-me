package ru.practicum.ewm.statsserver.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.statsserver.exception.model.ApiError;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

import static ru.practicum.ewm.statsserver.util.Constants.*;

@RestControllerAdvice
@Slf4j
public class ExceptionApiHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> validationException(ValidationException exception) {
        log.debug(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("BAD_REQUEST", "Incorrectly made request.",
                        exception.getMessage(), LocalDateTime.now().format(DATE_FORMAT)));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> runtimeException(RuntimeException exception) {
        log.debug(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("INTERNAL_SERVER_ERROR", "Something has gone wrong on server.",
                        exception.getMessage(), LocalDateTime.now().format(DATE_FORMAT)));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ApiError> handleValidation(Exception exception) {
        log.debug(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError("BAD_REQUEST", "Incorrectly made request.",
                        exception.getMessage(), LocalDateTime.now().format(DATE_FORMAT)));
    }
}

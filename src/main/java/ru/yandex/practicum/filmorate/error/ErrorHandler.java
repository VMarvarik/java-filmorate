package ru.yandex.practicum.filmorate.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.ValidationException;
import java.util.Map;

@RestControllerAdvice(value = "ru.yandex.practicum.filmorate.controller")
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIncorrectFormatError(final ValidationException e) {
        return Map.of(
                "error", "Ошибка с валидацией.",
                "errorMessage", e.getMessage()
        );
    }


    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInternalError(final RuntimeException e) {
        return Map.of(
                "error", "Внутренняя ошибка сервера",
                "errorMessage", e.getMessage()
        );
    }
}
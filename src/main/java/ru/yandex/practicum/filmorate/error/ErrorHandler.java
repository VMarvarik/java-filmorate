package ru.yandex.practicum.filmorate.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.ValidationException;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice(value = "ru.yandex.practicum.filmorate.controller")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIncorrectFormatError(final ValidationException e) {
        log.info("Сервер вернул код 400");
        return Map.of(
                "error", "Ошибка с валидацией.",
                "errorMessage", e.getMessage()
        );
    }


    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInternalError(final RuntimeException e) {
        log.info("Сервер вернул код 500");
        return Map.of(
                "error", "Внутренняя ошибка сервера",
                "errorMessage", e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundError(final NoSuchElementException e) {
        log.info("Сервер вернул код 404");
        return Map.of(
                "error", "Объект не найден",
                "errorMessage", e.getMessage()
        );
    }
}
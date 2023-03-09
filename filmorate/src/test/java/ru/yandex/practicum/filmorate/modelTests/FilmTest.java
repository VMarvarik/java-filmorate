package ru.yandex.practicum.filmorate.modelTests;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.Set;

public class FilmTest {
    private final Film correctFilmData = Film.builder().
            name("Сумерки")
            .description("Сага о вампирах")
            .releaseDate(LocalDate.parse("2008-11-20"))
            .duration(122)
            .build();
    private final Film incorrectFilmData = Film.builder().
            name("Сумерки")
            .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                    "Ut aliquet odio urna, quis commodo arcu laoreet ut. Donec posuere " +
                    "turpis id velit semper, sit amet vehicula ex vestibulum. Nunc venenatis fusce.")
            .releaseDate(LocalDate.parse("1008-11-20"))
            .duration(-122)
            .build();
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    void shouldCreateFilm() {
        Set<ConstraintViolation<Film>> validationViolations = validator.validate(correctFilmData);
        Assertions.assertTrue(validationViolations.isEmpty());
    }

    @Test
    void shouldNotCreateFilm() {
        Set<ConstraintViolation<Film>> validationViolations = validator.validate(incorrectFilmData);
        Assertions.assertEquals(3, validationViolations.size());
    }
}
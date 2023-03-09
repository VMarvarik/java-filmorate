package ru.yandex.practicum.filmorate.modelTests;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

public class UserTest {
    private final User correctUserData = User.builder()
            .id(1)
            .login("varya22")
            .name("Varya varya")
            .email("mail@mail.ru")
            .birthday(LocalDate.parse("2001-01-23"))
            .build();

    private final User incorrectUserData = User.builder()
            .id(1)
            .login(" ")
            .name("Varya varya")
            .email("mail@/////.")
            .birthday(null)
            .build();

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    void shouldCreateUser() {
        Set<ConstraintViolation<User>> validationViolations = validator.validate(correctUserData);
        Assertions.assertTrue(validationViolations.isEmpty());
    }

    @Test
    void shouldNotCreateUser() {
        Set<ConstraintViolation<User>> validationViolations = validator.validate(incorrectUserData);
        Assertions.assertEquals(4, validationViolations.size());
    }
}
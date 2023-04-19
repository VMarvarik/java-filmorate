package ru.yandex.practicum.filmorate.model;

import lombok.*;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validation.customAnnotation.NoSpaces;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@Slf4j
public class User {
    private Long id;

    @NotBlank(message = "email не может быть пустым или null")
    @Email(message = "Неправильный формат email")
    private String email;

    @NotBlank(message = "login не может быть пустым или null")
    @NoSpaces(message = "login не может содержать пробелы")
    private String login;

    private String name;

    @PastOrPresent(message = "birthday не может быть в будущем")
    private LocalDate birthday;

    private Set<Long> friends;

    private String nameCheck(String name, String login) {
        if (name == null || name.isBlank() ) {
            log.info("Логин был использован в качестве имени пользователя");
            return login;
        }
        return name;
    }
}
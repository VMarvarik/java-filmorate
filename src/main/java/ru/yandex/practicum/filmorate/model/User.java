package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validation.customAnnotation.NoSpaces;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@Builder
@Slf4j
public class User {
    private int id;

    @NotBlank(message = "email не может быть пустым или null")
    @Email(message = "Неправильный формат email")
    private String email;

    @NotBlank(message = "login не может быть пустым или null")
    @NoSpaces(message = "login не может содержать пробелы")
    private String login;

    private String name;

    @PastOrPresent(message = "birthday не может быть в будущем")
    private LocalDate birthday;

    private String nameCheck(String name, String login) {
        if (name == null || name.isBlank() ) {
            log.info("Логин был использован в качестве имени пользователя");
            return login;
        }
        return name;
    }

    @JsonCreator
    public User (@JsonProperty("id") int id,
                 @JsonProperty("email") String email,
                 @JsonProperty("login") String login,
                 @JsonProperty("name") String name,
                 @JsonProperty("birthday") LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = nameCheck(name, login);
        this.birthday = birthday;
    }
}
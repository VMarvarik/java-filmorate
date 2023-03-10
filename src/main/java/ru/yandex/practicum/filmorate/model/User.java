package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validation.customAnnotation.NoSpaces;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
@Slf4j
public class User {
    private int id;

    @NotNull(message = "email не может быть null")
    @NotBlank(message = "email не может быть пустым")
    @Email(message = "Неправильный формат email")
    private String email;

    @NotNull(message = "login не может быть null")
    @NotBlank(message = "login не может быть пустым")
    @NoSpaces(message = "login не может содержать пробелы")
    private String login;

    private String name;

    @NotNull(message = "birthday не может быть null")
    @Past(message = "birthday не может быть в будущем")
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
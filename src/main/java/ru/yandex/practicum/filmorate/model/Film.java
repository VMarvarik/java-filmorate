package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validation.customAnnotation.AfterBirthOfCinema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Film {

    private int id;

    @NotNull(message = "name не может быть null")
    @NotBlank(message = "name не может быть пустым")
    private String name;

    @NotNull(message = "description не может быть null")
    @NotBlank(message = "description не может быть пустым")
    @Size(message = "количество знаков для description не должно превышать 200", max = 200)
    private String description;

    @NotNull(message = "releaseDate не может быть null")
    @AfterBirthOfCinema(message = "releaseDate не может быть до даты появления кино")
    private LocalDate releaseDate;

    @Positive(message = "duration не может быть отрицательным значением")
    private int duration;
}
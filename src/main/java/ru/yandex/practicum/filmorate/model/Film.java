package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validation.customAnnotation.AfterBirthOfCinema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Film {

    private int id;

    @NotBlank(message = "name не может быть пустым или null")
    private String name;

    @Size(message = "количество знаков для description не должно превышать 200", max = 200)
    private String description;

    @AfterBirthOfCinema(message = "releaseDate не может быть до даты появления кино")
    private LocalDate releaseDate;

    @Positive(message = "duration не может быть отрицательным значением")
    private int duration;
}
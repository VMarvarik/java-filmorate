package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Rating {
    private Integer id;
    private String name;
}
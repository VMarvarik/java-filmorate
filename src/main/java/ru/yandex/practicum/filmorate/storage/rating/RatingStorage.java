package ru.yandex.practicum.filmorate.storage.rating;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface RatingStorage {

    Rating getRatingById(Integer mpaId);

    List<Rating> getRatingAll();
}
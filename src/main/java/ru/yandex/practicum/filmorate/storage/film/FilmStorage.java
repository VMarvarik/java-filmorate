package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Map;

public interface FilmStorage extends Storage<Film> {
    Map<Long, Film> getFilmMap();
}
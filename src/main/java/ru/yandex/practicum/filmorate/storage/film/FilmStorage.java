package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.HashMap;

public interface FilmStorage {
    Film addFilm(Film film);
    Film updateFilm(Film film);
    Film getFilmById(Long id);
    void deleteFilm(Long id);
    Collection<Film> getAllFilms();
    HashMap<Long, Film> getFilmHashMap();
}
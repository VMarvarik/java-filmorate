package ru.yandex.practicum.filmorate.storage.genres;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenresStorage {
    Genre getGenreById(Integer genreId);

    List<Genre> getAllGenres();

    void createGenre(Genre genre);

    Set<Genre> getGenresOfFilm(Long filmId);
}
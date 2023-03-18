package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addFilm(Film film) {
        return filmStorage.add(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    public Film getFilmById(Long id) {
        return filmStorage.getById(id);
    }

    public void deleteFilm(Long id) {
        filmStorage.delete(id);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public void addLike(Long id, Long userId) {
        if (containsFilm(id) && containsUser(userId)) {
            filmStorage.getById(id).getLikes().add(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void removeLike(Long id, Long userId) {
        if (containsFilm(id) && containsUser(userId)) {
            filmStorage.getById(id).getLikes().remove(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Collection<Film> getPopularFilms(Integer count) {
        Collection<Film> popularFilms = getAllFilms();
        return popularFilms.stream()
                .sorted((film1,film2) -> film2.getAmountOfLikes() - film1.getAmountOfLikes())
                .limit(count)
                .collect(Collectors.toList());
    }

    private boolean containsUser(Long id) {
        return userStorage.contains(id);
    }

    private boolean containsFilm(Long id) {
        return filmStorage.contains(id);
    }
}
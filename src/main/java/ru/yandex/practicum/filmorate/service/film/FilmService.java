package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;

    public Film addFilm(Film film) {
        return filmStorage.add(film);
    }

    public Film updateFilm(Film film) {
        if (containsFilm(film.getId())) {
            if (film.getGenres() != null) {
                List<Genre> newGenres = film.getGenres().stream()
                        .collect(Collectors.collectingAndThen(Collectors.toList(), lst -> {
                            Collections.reverse(lst);
                            return lst.stream();
                        }))
                        .collect(Collectors.toList());
                film.setGenres(new HashSet<>(newGenres));
            }
            return filmStorage.update(film);
        }
        log.info("Фильм " + film.getId() + " не найден");
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Film getFilmById(Long id) {
        if (containsFilm(id)) {
            return filmStorage.getById(id);
        }
        log.info("Фильм " + id + " не найден");
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void deleteFilm(Long id) {
        if (containsFilm(id)) {
            filmStorage.delete(id);
        }
        log.info("Фильм " + id + " не найден");
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public void addLike(Long id, Long userId) {
        if (containsFilm(id)) {
            if (containsUser(userId)) {
                likesStorage.addLike(id, userId);
            } else {
                log.info("Пользователь " + userId + " не найден");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else {
            log.info("Фильм " + id + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void removeLike(Long id, Long userId) {
        if (containsFilm(id)) {
            if (containsUser(userId)) {
                likesStorage.removeLike(id, userId);
            } else {
                log.info("Пользователь " + userId + " не найден");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else {
            log.info("Фильм " + id + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return likesStorage.getTopFilmLikes().
                stream().
                limit(count).
                map(this::getFilmById).
                collect(Collectors.toList());
    }

    private boolean containsUser(Long id) {
        return userStorage.getUsersMap().containsKey(id);
    }

    private boolean containsFilm(Long id) {
        return filmStorage.getFilmMap().containsKey(id);
    }
}
package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> filmHashMap = new HashMap<>();
    private Long id = 0L;

    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        filmHashMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmHashMap.containsKey(film.getId())) {
            filmHashMap.put(film.getId(), film);
            log.debug("Фильм {} был обновлен", film.getId());
            return film;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public Film getFilmById(Long id) {
        if (filmHashMap.containsKey(id)) {
            return filmHashMap.get(id);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public void deleteFilm(Long id) {
        if (filmHashMap.containsKey(id)) {
            filmHashMap.remove(id);
            log.debug("Фильм {} был удален", id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        return new ArrayList<>(filmHashMap.values());
    }

    @Override
    public HashMap<Long, Film> getFilmHashMap() {
        return filmHashMap;
    }
}
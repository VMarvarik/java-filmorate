package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> filmHashMap = new HashMap<>();
    private Long id = 0L;


    @Override
    public Film add(@Valid Film film) {
        if (film != null) {
            film.setId(++id);
            filmHashMap.put(film.getId(), film);
            log.info("Фильм добавлен");
            return film;
        }
        log.info("Объект фильм был null");
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Film update(@Valid Film film) {
        if (film != null){
            if (filmHashMap.containsKey(film.getId())) {
                filmHashMap.put(film.getId(), film);
                log.info("Фильм обновлен");
                return film;
            }
            log.info("Фильм " + film.getId() + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("Объект пользователь был null");
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Film getById(Long id) {
        if (filmHashMap.containsKey(id)) {
            return filmHashMap.get(id);
        }
        log.info("Фильм " + id + " не найден");
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public void delete(Long id) {
        if (filmHashMap.containsKey(id)) {
            filmHashMap.remove(id);
        } else {
            log.info("Фильм " + id + " не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Collection<Film> getAll() {
        return new ArrayList<>(filmHashMap.values());
    }

    @Override
    public Boolean contains(Long id){
        return filmHashMap.containsKey(id);
    }
}
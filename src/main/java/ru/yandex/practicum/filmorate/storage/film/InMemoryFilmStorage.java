package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> filmHashMap = new HashMap<>();
    private Long id = 0L;


    @Override
    public Film add(@Valid Film film) {
        film.setId(++id);
        filmHashMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(@Valid Film film) {
        if (filmHashMap.containsKey(film.getId())) {
            filmHashMap.put(film.getId(), film);
            return film;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public Film getById(Long id) {
        if (filmHashMap.containsKey(id)) {
            return filmHashMap.get(id);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public void delete(Long id) {
        if (filmHashMap.containsKey(id)) {
            filmHashMap.remove(id);
        } else {
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
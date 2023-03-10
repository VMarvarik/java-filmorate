package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private HashMap<Integer, Film> filmHashMap = new HashMap<>();
    private int id = 0;
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(++id);
        filmHashMap.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (filmHashMap.containsKey(film.getId())) {
            filmHashMap.put(film.getId(), film);
            log.debug("Фильм {} был обновлен", film.getId());
            return film;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ArrayList<Film> getAllFilms() {
        log.debug("Текущее количество фильмов: {}", filmHashMap.size());
        return new ArrayList<>(filmHashMap.values());
    }
}

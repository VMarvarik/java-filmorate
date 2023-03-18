package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @DeleteMapping
    public void deleteFilm(@PathVariable Long id) {
        filmService.deleteFilm(id);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping(value = "/{id}")
    public Film getFilmById(@Valid @PathVariable Long id) {
        return filmService.getFilmById(id);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void addLike(@Valid @PathVariable Long id, @Valid @PathVariable Long userId) {
        filmService.addLike(id, userId);
        log.debug("Like is added");
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void removeLike(@Valid @PathVariable Long id, @Valid @PathVariable Long userId) {
        filmService.removeLike(id, userId);
        log.debug("Like is removed");
    }

    @GetMapping(value = "/popular")
    public Collection<Film> getPopular(@RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }
}
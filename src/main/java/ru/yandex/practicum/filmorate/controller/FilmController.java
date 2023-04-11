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
        log.info("Добавление фильма...");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Обновление фильма " + film.getId() + "...");
        return filmService.updateFilm(film);
    }

    @DeleteMapping
    public void deleteFilm(@PathVariable Long id) {
        log.info("Удаление фильма " + id + "...");
        filmService.deleteFilm(id);
        log.info("Фильм удален");
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Вызов всех фильмов...");
        return filmService.getAllFilms();
    }

    @GetMapping(value = "/{id}")
    public Film getFilmById(@Valid @PathVariable Long id) {
        log.info("Вызов фильма по id:" + id + "...");
        return filmService.getFilmById(id);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void addLike(@Valid @PathVariable Long id, @Valid @PathVariable Long userId) {
        log.info("Добавление лайка пользователем " + userId + " фильму " + id + "...");
        filmService.addLike(id, userId);
        log.info("Лайк добавлен");
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void removeLike(@Valid @PathVariable Long id, @Valid @PathVariable Long userId) {
        log.info("Удаление лайка пользователем " + userId + " фильму " + id + "...");
        filmService.removeLike(id, userId);
        log.info("Лайк удален");
    }

    @GetMapping(value = "/popular")
    public Collection<Film> getPopular
            (@RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
        log.info("Вызов популярных фильмов...");
        return filmService.getPopularFilms(count);
    }
}
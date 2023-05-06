package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Количество добавленных фильмов: " + filmService.getFilmStorage().getFilms().size());
        return filmService.getFilmStorage().getFilms();
    }

    @PostMapping
    public Film addFilms(@RequestBody @Valid Film film) throws ValidationException {
        return filmService.getFilmStorage().addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) throws NotFoundException {
        return filmService.getFilmStorage().updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.getLikeStorage().addLike(id, userId);
        return filmService.getFilmStorage().getFilmById(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.getLikeStorage().deleteLike(id, userId);
        return filmService.getFilmStorage().getFilmById(id);
    }

    @GetMapping("/popular")
    public Set<Film> getFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getLikeStorage().getIdPopularsFilm(count);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmStorage().getFilmById(id);
    }
}

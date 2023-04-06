package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Количество добавленных фильмов: " + inMemoryFilmStorage.getFilms().size());
        return inMemoryFilmStorage.getFilms();
    }

    @PostMapping
    public Film addFilms(@RequestBody @Valid Film film) throws ValidationException {
        return inMemoryFilmStorage.addFilm(film);
    }

    @PutMapping
    public Film saveFilm(@RequestBody @Valid Film film) throws ValidationException {
        return inMemoryFilmStorage.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Set<Film> getFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        return inMemoryFilmStorage.getFilmById(id);
    }
}

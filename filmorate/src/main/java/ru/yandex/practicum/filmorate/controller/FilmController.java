package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Количество добавленных фильмов: " + films.size());
        return films.values();
    }

    @PostMapping
    @ResponseBody
    public Film addFilms(@RequestBody @Valid Film film) throws ValidationException {
        if (isCanSave(film)) {
            film.setId(films.size() + 1);
            films.put(film.getId(), film);
            log.info("Фильм сохранен: {}", film);
            return film;
        } else {
            log.info("Фильм не сохранен: {}", film);
            throw new ValidationException("Такой фильм не может быть добавлен");
        }
    }

    @PutMapping
    @ResponseBody
    public Film saveFilm(@RequestBody @Valid Film film) throws ValidationException{
        if (isCanSave(film) && films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм обновлен: {}", film);
            return film;
        } else {
            log.info("Фильм не обновлен: {}", film);
            throw new ValidationException("Такой фильм не может быть обновлен");
        }

    }

    public boolean isCanSave(Film film) {
        if (film.getDescription().length() > 200) return false;
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) return false;
        return !(film.getDuration() < 0);
    }

}

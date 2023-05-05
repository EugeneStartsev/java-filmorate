package ru.yandex.practicum.filmorate.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.dao.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmControllerTest {
    FilmController filmController;
    FilmService filmService;
    FilmStorage filmStorage;
    UserStorage userStorage;

    @BeforeEach
    public void beforeEach() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    public void shouldThrowException() {
        try {
            Film film = new Film(1, "1", "1", LocalDate.parse("1890-11-11"), 200,1);
            filmController.addFilms(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Такой фильм не может быть добавлен", e.getMessage());
        }

        try {
            Film film = new Film(1, "1", "                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ",
                    LocalDate.parse("1890-11-11"), 200, 2);
            filmController.addFilms(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Такой фильм не может быть добавлен", e.getMessage());
        }

        try {
            Film film = new Film(1, "1", "1", LocalDate.parse("1890-11-11"), -200, 3);
            filmController.addFilms(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Такой фильм не может быть добавлен", e.getMessage());
        }

        try {
            Film film = new Film(1, "", "1", LocalDate.parse("1890-11-11"), 200, 4);
            filmController.addFilms(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Такой фильм не может быть добавлен", e.getMessage());
        }
    }
}

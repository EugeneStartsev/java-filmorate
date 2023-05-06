package ru.yandex.practicum.filmorate.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.impl.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.inMemoryStorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.inMemoryStorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmControllerTest {
    FilmController filmController;
    FilmService filmService;
    FilmStorage filmStorage;
    UserStorage userStorage;
    LikeDbStorage likeDbStorage;
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void beforeEach() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        likeDbStorage = new LikeDbStorage(jdbcTemplate, filmStorage);
        filmService = new FilmService(filmStorage, userStorage, likeDbStorage);
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

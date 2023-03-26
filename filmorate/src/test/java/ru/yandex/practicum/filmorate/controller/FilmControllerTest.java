package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    public void shouldThrowException() throws ValidationException {
        try {
            Film film = new Film(1, "1", "1", LocalDate.parse("1890-11-11"), 200);
            filmController.addFilms(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Такой фильм не может быть добавлен", e.getMessage());
        }

        try {
            Film film = new Film(1, "1", "                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ",
                    LocalDate.parse("1890-11-11"), 200);
            filmController.addFilms(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Такой фильм не может быть добавлен", e.getMessage());
        }

        try {
            Film film = new Film(1, "1", "1", LocalDate.parse("1890-11-11"), -200);
            filmController.addFilms(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Такой фильм не может быть добавлен", e.getMessage());
        }

        try {
            Film film = new Film(1, "", "1", LocalDate.parse("1890-11-11"), 200);
            filmController.addFilms(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Такой фильм не может быть добавлен", e.getMessage());
        }
    }
}

package ru.yandex.practicum.filmorate.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.time.LocalDate;
import java.time.Month;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureTestDatabase
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerTest {
    final FilmController filmController;

    @Test
    public void shouldAdd() {
        Film film = Film.builder()
                .name("ashdkj")
                .description("asdjlkaskl")
                .releaseDate(LocalDate.of(1990, Month.APRIL, 16))
                .duration(180)
                .mpa(new Rating(1, "hhahah"))
                .build();
        filmController.addFilms(film);
       Assertions.assertEquals(1, filmController.getFilms().size());
    }

    @Test
    public void shouldNotAddByDate() {
        Film film = Film.builder()
                .name("ashdkj")
                .description("asdjlkaskl")
                .releaseDate(LocalDate.of(0001, Month.APRIL, 16))
                .duration(180)
                .mpa(new Rating(1, "hhahah"))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilms(film));
    }

    @Test
    public void shouldNotAddByDuration() {
        Film film = Film.builder()
                .name("ashdkj")
                .description("asdjlkaskl")
                .releaseDate(LocalDate.of(2001, Month.APRIL, 16))
                .duration(-1)
                .mpa(new Rating(1, "hhahah"))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilms(film));
    }
}

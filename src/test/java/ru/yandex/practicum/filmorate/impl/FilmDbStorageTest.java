package ru.yandex.practicum.filmorate.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashSet;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureTestDatabase
public class FilmDbStorageTest {
    final FilmDbStorage filmDbStorage;

    @Test
    public void saveTest() {
        Film expectedFilm = Film.builder()
                .name("ashdkj")
                .description("asdjlkaskl")
                .releaseDate(LocalDate.of(1990, Month.APRIL, 16))
                .duration(180)
                .mpa(new Rating(1, "G"))
                .genres(new HashSet<>())
                .build();
        filmDbStorage.addFilm(expectedFilm);

        Film returnedFilm = filmDbStorage.getFilmById(1);

        Assertions.assertEquals(expectedFilm, returnedFilm);
    }

    @Test
    public void updateTest() {
        Film expectedFilm = Film.builder()
                .id(1)
                .name("ashdkj")
                .description("1111")
                .releaseDate(LocalDate.of(1990, Month.APRIL, 16))
                .duration(180)
                .mpa(new Rating(1, "G"))
                .genres(new HashSet<>())
                .build();

        Film beforeFilm = Film.builder()
                .name("ashdkj")
                .description("asdjlkaskl")
                .releaseDate(LocalDate.of(1990, Month.APRIL, 16))
                .duration(180)
                .mpa(new Rating(1, "G"))
                .genres(new HashSet<>())
                .build();

        filmDbStorage.addFilm(beforeFilm);

        filmDbStorage.updateFilm(expectedFilm);

        Film returnedFilm = filmDbStorage.getFilmById(1);

        Assertions.assertEquals(expectedFilm, returnedFilm);
    }

    @Test
    public void deleteTest() {
        Film film = Film.builder()
                .name("ashdkj")
                .description("asdjlkaskl")
                .releaseDate(LocalDate.of(1990, Month.APRIL, 16))
                .duration(180)
                .mpa(new Rating(1, "G"))
                .genres(new HashSet<>())
                .build();

        filmDbStorage.addFilm(film);
        filmDbStorage.deleteFilm(film);

        Collection<Film> films = filmDbStorage.getFilms();

        Assertions.assertEquals(2, films.size());
    }
}

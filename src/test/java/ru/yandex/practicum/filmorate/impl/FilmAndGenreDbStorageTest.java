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
import ru.yandex.practicum.filmorate.model.FilmAndGenre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.impl.FilmAndGenreDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmAndGenreDbStorageTest {
    final FilmAndGenreDbStorage filmAndGenreDbStorage;
    final FilmDbStorage filmDbStorage;

    @Test
    public void test() {
        Film film = Film.builder()
                .name("ashdkj")
                .description("asdjlkaskl")
                .releaseDate(LocalDate.of(1990, Month.APRIL, 16))
                .duration(180)
                .mpa(new Rating(1, "hhahah"))
                .build();
        filmDbStorage.addFilm(film);

        filmAndGenreDbStorage.addGenre(1, 1);

        List<FilmAndGenre> filmAndGenreList = filmAndGenreDbStorage.getAllGenresByFilmId(1);

        Assertions.assertEquals(1, filmAndGenreList.get(0).getFilmId());
        Assertions.assertEquals(1, filmAndGenreList.get(0).getGenreId());
    }
}

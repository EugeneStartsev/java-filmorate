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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureTestDatabase
public class LikeDbStorageTest {
    final FilmDbStorage filmDbStorage;
    final UserDbStorage userDbStorage;
    final LikeDbStorage likeDbStorage;

    @Test
    public void test() {
        User user = User.builder()
                .name("User")
                .email("123lol@yandex.ru")
                .birthday(LocalDate.of(1991, Month.JULY, 23))
                .login("STwix")
                .build();

        Film film = Film.builder()
                .name("ashdkj")
                .description("asdjlkaskl")
                .releaseDate(LocalDate.of(1990, Month.APRIL, 16))
                .duration(180)
                .mpa(new Rating(1, "G"))
                .genres(new HashSet<>())
                .build();

        filmDbStorage.addFilm(film);
        userDbStorage.addUser(user);

        likeDbStorage.addLike(1, 1);

        Set<Film> popularFilms = likeDbStorage.getIdPopularsFilm(5);

        Assertions.assertEquals(3, popularFilms.size());
    }
}

package ru.yandex.practicum.filmorate.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorage;

import java.util.Collection;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureTestDatabase
public class GenreDbStorageTest {
    final GenreDbStorage genreDbStorage;

    @Test
    public void geyById() {
        Genre expectedGenre = genreDbStorage.getGenreById(1);

        Assertions.assertEquals(1, expectedGenre.getId());
    }

    @Test
    public void getAll() {
        Collection<Genre> genres = genreDbStorage.getGenres();

        Assertions.assertEquals(6, genres.size());
    }
}

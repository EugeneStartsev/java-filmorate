package ru.yandex.practicum.filmorate.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.impl.RatingDbStorage;

import java.util.Collection;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureTestDatabase
public class RatingDbStorageTest {
    final RatingDbStorage ratingDbStorage;

    @Test
    public void geyById() {
        Rating expectedRating = ratingDbStorage.getRatingById(1);

        Assertions.assertEquals(1, expectedRating.getId());
    }

    @Test
    public void getAll() {
        Collection<Rating> genres = ratingDbStorage.getRatings();

        Assertions.assertEquals(5, genres.size());
    }
}

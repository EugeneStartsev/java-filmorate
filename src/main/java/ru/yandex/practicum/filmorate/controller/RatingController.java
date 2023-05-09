package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.interfaces.RatingStorage;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/mpa")
public class RatingController {
    private final RatingStorage ratingStorage;

    @GetMapping("{id}")
    public Rating getRatingById(@PathVariable int id) {
        return ratingStorage.getRatingById(id);
    }

    @GetMapping
    public Collection<Rating> getAllRating() {
        return ratingStorage.getRatings();
    }
}

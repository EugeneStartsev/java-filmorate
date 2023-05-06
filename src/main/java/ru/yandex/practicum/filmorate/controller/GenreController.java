package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {
    private final GenreStorage genreStorage;

    @GetMapping("{id}")
    public Genre getGenreById(@PathVariable int id) {
        return genreStorage.getGenreById(id);
    }

    @GetMapping
    public Collection<Genre> getAllGenre() {
        return genreStorage.getGenres();
    }
}

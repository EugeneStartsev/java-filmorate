package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.FilmAndGenre;

import java.util.List;

public interface FilmAndGenreStorage {
    void addGenre(int genreId, int filmId);

    List<FilmAndGenre> getAllGenresByFilmId(int filmId);
}

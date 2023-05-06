package ru.yandex.practicum.filmorate.storage.interfaces;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Component
public interface FilmStorage {
    Film addFilm(Film film);

    Film deleteFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getFilms();

    Film getFilmById(int id);

    boolean isCanSaveFilm(Film film);
}

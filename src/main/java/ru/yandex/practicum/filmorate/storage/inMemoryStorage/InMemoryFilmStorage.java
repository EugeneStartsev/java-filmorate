package ru.yandex.practicum.filmorate.storage.inMemoryStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    public Film addFilm(Film film) {
        if (isCanSaveFilm(film)) {
            film.setId(films.size() + 1);
            films.put(film.getId(), film);
            log.info("Фильм сохранен: {}", film);
            return film;
        } else {
            log.info("Фильм не сохранен: {}", film);
            throw new ValidationException("Такой фильм не может быть добавлен");
        }
    }

    public Film updateFilm(Film film) {
        if (isCanSaveFilm(film) && films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм обновлен: {}", film);
            return film;
        } else {
            log.info("Фильм не обновлен: {}", film);
            throw new NotFoundException("Такой фильм не может быть обновлен");
        }
    }

    public Film deleteFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            log.info("Фильм успешно удален: {}", film);
            return film;
        } else {
            log.info("Фильм не был удален: {}", film);
            throw new NotFoundException("Такой фильм не получилось удалить");
        }
    }

    public Collection<Film> getFilms() {
        return films.values();
    }

    public Film getFilmById(int id) {
        if (films.containsKey(id)) return films.get(id);
        else throw new NotFoundException(String.format("Фильма с таким ID %s нет", id));
    }

    public boolean isCanSaveFilm(Film film) {
        if (film.getDescription().length() > 200) return false;
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) return false;
        return !(film.getDuration() < 0);
    }
}

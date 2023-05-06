package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmAndGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmAndGenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.RatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RatingStorage ratingStorage;
    private final GenreStorage genreStorage;
    private final FilmAndGenreStorage filmAndGenreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RatingStorage ratingStorage,
                         FilmAndGenreStorage filmAndGenreStorage, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.ratingStorage = ratingStorage;
        this.filmAndGenreStorage = filmAndGenreStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film getFilmById(int id) {
        String sqlQuery = "SELECT* FROM films WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
    }

    @Override
    public Collection<Film> getFilms() {
        String sqlQuery = "SELECT* FROM films";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film addFilm(Film film) {
        if (isCanSaveFilm(film)) {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("films")
                    .usingGeneratedKeyColumns("film_id");

            Map<String, Object> filmParameters = new HashMap<>();
            filmParameters.put("name", film.getName());
            filmParameters.put("description", film.getDescription());
            filmParameters.put("release_date", film.getReleaseDate());
            filmParameters.put("duration", film.getDuration());
            filmParameters.put("rating_id", film.getMpa().getId());

            film.setId(simpleJdbcInsert.executeAndReturnKey(filmParameters).intValue());

            if (film.getGenres() != null) {
                film.getGenres().forEach(genre -> filmAndGenreStorage.save(genre.getId(), film.getId()));
            }

            return film;
        } else throw new ValidationException("Такой фильм не может быть сохранен");
    }

    @Override
    public Film deleteFilm(Film film) {
        int id = film.getId();
        String sqlQuery = "DELETE FROM films WHERE film_id = ?";
        if (jdbcTemplate.update(sqlQuery, id) > 0) return film;
        else throw new NotFoundException("Такого невозможно удалить");
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films " + "SET name = ?" + ", description = ?" + ", release_date = ?" +
                ", duration = ? " + ", rating_id = ?" + "WHERE film_id = ?";
            String sqlDeleteQuery = "DELETE FROM film_genre WHERE film_id = ?";

            jdbcTemplate.update(sqlDeleteQuery, film.getId());
            if (film.getGenres() != null) {
                film.getGenres().forEach(genre -> filmAndGenreStorage.save(genre.getId(), film.getId()));
            }

            if (jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId()) > 0) return film;
            else throw new NotFoundException("Такой пользователь не может быть обновлен");
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Rating rating = ratingStorage.getRatingById(resultSet.getInt("rating_id"));
        Set<Genre> genres = new HashSet<>();
        for (FilmAndGenre filmAndGenre : filmAndGenreStorage.getAllGenresByFilmId(resultSet.getInt("film_id"))) {
            genres.add(genreStorage.getGenreById(filmAndGenre.getGenreId()));
        }
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(rating)
                .genres(genres)
                .build();
    }

    public boolean isCanSaveFilm(Film film) {
        if (film.getDescription().length() > 200) return false;
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) return false;
        return !(film.getDuration() < 0);
    }
}

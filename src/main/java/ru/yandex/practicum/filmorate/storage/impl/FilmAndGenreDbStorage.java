package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmAndGenre;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmAndGenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Component
public class FilmAndGenreDbStorage implements FilmAndGenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmAndGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<FilmAndGenre> getAllGenresByFilmId(int filmId) {
        String sqlQuery = "SELECT* FROM film_genre WHERE film_id = ?";
        return jdbcTemplate.query(sqlQuery, this::makeRowToFilmAndGenre, filmId);
    }

    @Override
    public void save(int genreId, int filmId) {
        String sqlQuery = "INSERT INTO film_genre (film_id , genre_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    private FilmAndGenre makeRowToFilmAndGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new FilmAndGenre(resultSet.getInt("film_id"), resultSet.getInt("genre_id"));
    }
}

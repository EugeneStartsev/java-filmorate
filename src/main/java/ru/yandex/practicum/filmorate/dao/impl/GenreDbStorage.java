package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getGenreById(Integer id) {
        String sqlQuery = "SELECT* FROM genres WHERE genre_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::makeRowToGenre, id);
    }

    public Collection<Genre> getGenres() {
        String sqlQuery = "SELECT* FROM genres";
        return jdbcTemplate.query(sqlQuery, this::makeRowToGenre);
    }

    private Genre makeRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"), resultSet.getString("name"));
    }
}

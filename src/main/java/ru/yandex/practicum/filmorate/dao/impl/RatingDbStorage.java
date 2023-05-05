package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.RatingStorage;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class RatingDbStorage implements RatingStorage {
    private final JdbcTemplate jdbcTemplate;

    public RatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Rating getRatingById(Integer id) {
        String sqlQuery = "SELECT* FROM rating WHERE rating_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::rowToRating, id);
    }

    @Override
    public Collection<Rating> getRatings() {
        String sqlQuery = "SELECT* FROM rating";
        return jdbcTemplate.query(sqlQuery, this::rowToRating);
    }

    private Rating rowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return new Rating(resultSet.getInt("rating_id"), resultSet.getString("name"));
    }
}

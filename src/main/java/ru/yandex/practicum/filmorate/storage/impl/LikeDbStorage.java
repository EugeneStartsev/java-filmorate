package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;

import java.util.HashSet;
import java.util.Set;

@Component
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate, FilmStorage filmStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sqlQuery = "INSERT INTO film_like (film_id, user_id) VALUES (?, ?)";
        if (jdbcTemplate.update(sqlQuery, filmId, userId) == 0)
            throw new NotFoundException("Лайк не может быть добавлен");
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sqlQuery = "DELETE FROM film_like WHERE film_id = ? AND user_id = ?";
        if (jdbcTemplate.update(sqlQuery, filmId, userId) == 0)
            throw new NotFoundException("Такой лайк с фильма не может быть удален");
    }

    @Override
    public Set<Film> getIdPopularsFilm(int count) {
        String sqlQuery = "SELECT film_id" +
                " FROM (SELECT films.film_id, COUNT(user_id) AS count_likes" +
                " FROM films" +
                " LEFT JOIN film_like AS likes on films.film_id = likes.film_id" +
                " GROUP BY films.film_id" +
                " ORDER BY count_likes DESC)" +
                " LIMIT ?";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQuery, count);

        Set<Film> popular = new HashSet<>();

        while (sqlRowSet.next()) {
            popular.add(filmStorage.getFilmById(sqlRowSet.getInt("film_id")));
        }

        return popular;
    }
}

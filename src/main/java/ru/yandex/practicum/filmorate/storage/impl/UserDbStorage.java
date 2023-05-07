package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(Integer id) {
        String sqlQuery = "SELECT* FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public User deleteUser(User user) {
        int id = user.getId();
        String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        if (jdbcTemplate.update(sqlQuery, id) > 0) return user;
        else throw new NotFoundException("Такого пользователя удалить не удалось");
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users " + "SET email = ?" + ", login = ?" + ", name = ?" +
                ", birthday = ? " + "WHERE user_id = ?";
        if (jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                user.getId()) > 0) return user;
        else throw new NotFoundException("Такой пользователь не может быть обновлен");
    }

    @Override
    public Collection<User> getUsers() {
        String sqlQuery = "SELECT* FROM users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User addUser(User user) {
        if (isCanSaveUser(user)) {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users")
                    .usingGeneratedKeyColumns("user_id");

            Map<String, Object> userParameters = new HashMap<>();
            userParameters.put("email", user.getEmail());
            userParameters.put("login", user.getLogin());
            if (user.getName() != null) {
                userParameters.put("name", user.getName());
            } else {
                userParameters.put("name", user.getLogin());
                user.setName(user.getLogin());
            }
            userParameters.put("birthday", user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            user.setId(simpleJdbcInsert.executeAndReturnKey(userParameters).intValue());

            return user;
        } else throw new ValidationException("Такой пользователь сохранен быть не может");
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    public boolean isCanSaveUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return !user.getBirthday().isAfter(LocalDate.now());
    }
}

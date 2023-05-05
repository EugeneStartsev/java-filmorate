package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final String updateUser = "UPDATE users " + "SET email = ?" + ", login = ?" + ", name = ?" +
            ", birthday = ? " + "WHERE user_id = ?";
    private final String allUsers = "SELECT* FROM users";

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
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public Collection<User> getUsers() {
        String sqlQuery = "SELECT* FROM users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        Map<String, Object> userParameters = new HashMap<>();
        userParameters.put("email", user.getEmail());
        userParameters.put("login", user.getLogin());
        if (user.getName() != null || !user.getName().isBlank()) {
            userParameters.put("name", user.getName());
        } else {
            userParameters.put("name", user.getLogin());
            user.setName(user.getLogin());
        }
        userParameters.put("birthday", user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        user.setId(simpleJdbcInsert.executeAndReturnKey(userParameters).intValue());

        return user;
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
}

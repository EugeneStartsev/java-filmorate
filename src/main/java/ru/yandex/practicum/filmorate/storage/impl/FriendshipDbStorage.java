package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate, UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public void addFriend(int firstUserId, int secondUserId) {
        String sqlQuery = "INSERT INTO friendship (first_user_id, second_user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, firstUserId, secondUserId);
    }

    @Override
    public void deleteFriend(int firstUserId, int secondUserId) {
        String sqlQuery = "DELETE FROM friendship WHERE first_user_id = ? AND second_user_id = ?";
        jdbcTemplate.update(sqlQuery, firstUserId, secondUserId);
    }

    @Override
    public List<User> getFriends(int userId) {
        String sqlQuery = "SELECT* FROM friendship WHERE first_user_id = ?";
        List<User> friends = new ArrayList<>();
        for (Friendship friendship : jdbcTemplate.query(sqlQuery, this::makeRowToFriendship, userId)) {
            friends.add(userStorage.getUserById(friendship.getSecondUserId()));
        }
        return friends;
    }

    private Friendship makeRowToFriendship(ResultSet resultSet, int rowNum) throws SQLException {
        return new Friendship(resultSet.getInt("first_user_id"),
                resultSet.getInt("second_user_id"));
    }
}

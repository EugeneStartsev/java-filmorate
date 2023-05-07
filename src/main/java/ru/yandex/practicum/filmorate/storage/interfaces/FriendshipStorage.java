package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {
    void addFriend(int firstUserId, int secondUserId);

    void deleteFriend(int firstUserId, int secondUserId);

    List<User> getFriends(int userId);
}

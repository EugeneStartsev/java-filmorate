package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Getter
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getFriends(Integer id) {
        List<User> friendList = new ArrayList<>();
        if (!userStorage.getUsers().isEmpty()) {
            User user = userStorage.getUserById(id);
            for (Integer i : user.getFriends()) {
                friendList.add(userStorage.getUserById(i));
            }
            log.info("Количество друзей: " + user.getFriends().size());
            return friendList;
        } else {
            throw new NullPointerException("Хранилище пустое");
        }
    }

    public User addFriend(Integer id, Integer friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);

        if (user != null && friend != null) {
            Set<Integer> userFriends = user.getFriends();
            userFriends.add(friendId);

            Set<Integer> friendFriends = friend.getFriends();
            friendFriends.add(id);

            log.info(String.format("Пользователь с ID %s успешно добавлен в друзья {}", friendId), user);

            return user;
        } else {
            log.info("Не удалось добавить пользователя в друзья: {}", user);
            throw new NotFoundException("Пользователя или друга с таким ID не существует");
        }
    }

    public User deleteFriend(Integer id, Integer friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);

        if (user.getFriends().contains(friendId)) {
            Set<Integer> userFriends = user.getFriends();
            userFriends.remove(friendId);
            user.setFriends(userFriends);

            Set<Integer> friendFriends = friend.getFriends();
            friendFriends.remove(id);
            friend.setFriends(friendFriends);

            log.info(String.format("Пользователь с ID %s успешно удален", friendId));

            return user;
        } else {
            log.info("Пользователь не находится в друзьях {}", user);
            throw new NotFoundException("Пользователь не находится в друзьях.");
        }
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        User user = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(otherId);
        List<User> commonFriends = new ArrayList<>();

        for (Integer i : user.getFriends()) {
            if (otherUser.getFriends().contains(i)) {
                commonFriends.add(userStorage.getUserById(i));
            }
        }
        log.info("Сформирован список общих друзей {}", commonFriends);
        return commonFriends;
    }
}

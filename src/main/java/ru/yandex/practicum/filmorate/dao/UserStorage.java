package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Component
public interface UserStorage {
    User addUser(User user);

    User deleteUser(User user);

    User updateUser(User user);

    Collection<User> getUsers();

    User getUserById(Integer id);
}

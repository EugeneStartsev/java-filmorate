package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    public User addUser(User user) {
        if (isCanSaveUser(user)) {
            user.setId(users.size() + 1);
            users.put(user.getId(), user);
            log.info("Пользователь сохранен: {}", user);
            return user;
        } else {
            log.info("Пользователь не сохранен: {}", user);
            throw new ValidationException("Такой пользователь не может быть добавлен");
        }
    }

    public User updateUser(User user) {
        if (isCanSaveUser(user) && users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь успешно обновлен: {}", user);
            return user;
        } else {
            log.info("Пользователь не обновлен: {}", user);
            throw new NotFoundException("Такой пользователь не может быть обновлен");
        }
    }

    public User deleteUser(User user) {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            log.info("Фильм успешно удален: {}", user);
            return user;
        } else {
            log.info("Фильм не был удален: {}", user);
            throw new NotFoundException("Такой фильм не получилось удалить");
        }
    }

    public Collection<User> getUsers() {
        return users.values();
    }

    public User getUserById(Integer id) {
        if (users.containsKey(id)) return users.get(id);
        else throw new NotFoundException(String.format("Фильма с таким ID %s нет", id));
    }

    public boolean isCanSaveUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return !user.getBirthday().isAfter(LocalDate.now());
    }
}

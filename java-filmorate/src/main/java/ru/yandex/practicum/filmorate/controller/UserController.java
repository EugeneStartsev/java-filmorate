package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Количество пользователей: " + users.size());
        return users.values();
    }

    @PostMapping
    @ResponseBody
    public User saveUser(@Valid @RequestBody User user) throws ValidationException {
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

    @PutMapping
    @ResponseBody
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (isCanSaveUser(user) && users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь успешно обновлен: {}", user);
            return user;
        } else {
            log.info("Пользователь не обновлен: {}", user);
            throw new ValidationException("Такой пользователь не может быть обновлен");
        }
    }

    public boolean isCanSaveUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return !user.getBirthday().isAfter(LocalDate.now());
    }
}

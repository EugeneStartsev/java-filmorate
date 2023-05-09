package ru.yandex.practicum.filmorate.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureTestDatabase
public class UserDbStorageTest {
    final UserDbStorage userDbStorage;

    @Test
    public void saveTest() {
        User expectedUser = User.builder()
                .name("UsersFriend")
                .email("friend@gmail.com")
                .birthday(LocalDate.of(1992, Month.JUNE, 25))
                .login("Salviux")
                .build();

        userDbStorage.addUser(expectedUser);

        User returnedUser = userDbStorage.getUserById(3);

        Assertions.assertEquals(expectedUser, returnedUser);
    }

    @Test
    public void updateTest() {
        User beforeUser = User.builder()
                .name("UsersFriend")
                .email("friend@gmail.com")
                .birthday(LocalDate.of(1992, Month.JUNE, 25))
                .login("Salviux")
                .build();

        User expectedUser = User.builder()
                .id(1)
                .name("UsersFriend Update")
                .email("update@gmail.com")
                .birthday(LocalDate.of(1992, Month.JUNE, 25))
                .login("Salviux")
                .build();

        userDbStorage.addUser(beforeUser);
        userDbStorage.updateUser(expectedUser);
        User returnedUser = userDbStorage.getUserById(1);

        Assertions.assertEquals(expectedUser, returnedUser);
    }

    @Test
    public void deleteUser() {
        User user = User.builder()
                .name("UsersFriend")
                .email("friend@gmail.com")
                .birthday(LocalDate.of(1992, Month.JUNE, 25))
                .login("Salviux")
                .build();

        userDbStorage.addUser(user);
        userDbStorage.deleteUser(user);

        Collection<User> users = userDbStorage.getUsers();

        Assertions.assertEquals(4, users.size());
    }
}

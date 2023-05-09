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
import ru.yandex.practicum.filmorate.storage.impl.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureTestDatabase
public class FriendshipDbStorageTest {
    final FriendshipDbStorage friendshipDbStorage;
    final UserDbStorage userDbStorage;

    @Test
    public void test() {
        User firstUser = User.builder()
                .name("User")
                .email("123lol@yandex.ru")
                .birthday(LocalDate.of(1991, Month.JULY, 23))
                .login("STwix")
                .build();

        User friend = User.builder()
                .name("UsersFriend")
                .email("friend@gmail.com")
                .birthday(LocalDate.of(1992, Month.JUNE, 25))
                .login("Salviux")
                .build();

        userDbStorage.addUser(firstUser);
        userDbStorage.addUser(friend);

        friendshipDbStorage.addFriend(1, 2);

        List<User> firstUsersFriends = friendshipDbStorage.getFriends(1);

        Assertions.assertEquals(2, firstUsersFriends.get(0).getId());
    }
}

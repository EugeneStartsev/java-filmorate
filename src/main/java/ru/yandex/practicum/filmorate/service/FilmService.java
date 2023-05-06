package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
@Slf4j
@Getter
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    public Film addLike(Integer id, Integer userId) {
        Film film = filmStorage.getFilmById(id);
        User user = userStorage.getUserById(userId);

        if (film != null && user != null) {
            Set<Integer> likes = film.getLikes();
            likes.add(userId);
            film.setLikes(likes);
            log.info(String.format("Лайк фильму %s поставлен от пользователя с ID %s ", film.getName(), userId));
            return film;
        } else {
            log.info("Не удалось добавить лайк");
            throw new NotFoundException("Фильма или пользователя с таким ID не существует");
        }
    }

    public Film deleteLike(Integer id, Integer userId) {
        Film film = filmStorage.getFilmById(id);

        if (film.getLikes().contains(userId)) {
            Set<Integer> likes = film.getLikes();
            likes.remove(userId);
            film.setLikes(likes);

            log.info(String.format("Лайк пользователя с ID %s успешно удален", userId));
            return film;
        } else {
            log.info("Пользователя с таким ID не удалось найти");
            throw new NotFoundException(String.format("Пользователь с ID %s не ставил лайк ", userId));
        }
    }

    public Set<Film> getFilms(Integer count) {
        Set<Film> films = new HashSet<>();
        Map<Integer, Integer> sortedLikes = sortedFilmsByLikes();
        int i = 0;
        for (Integer id : sortedLikes.keySet()) {
            if (i < count) {
                films.add(filmStorage.getFilmById(id));
                i++;
            } else break;
        }
        return films;
    }

    private Map<Integer, Integer> sortedFilmsByLikes() {
        Comparator<Integer> comparator = Comparator.reverseOrder();
        Map<Integer, Integer> likes = new TreeMap<>(comparator);
        for (int i = 0; i < filmStorage.getFilms().size(); i++) {
            likes.put(filmStorage.getFilmById(i + 1).getId(), filmStorage.getFilmById(i + 1).getLikes().size());
        }
        return likes;
    }
}

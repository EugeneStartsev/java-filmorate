package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FIlmAndGenre {
    private int filmId;
    private int genreId;
}

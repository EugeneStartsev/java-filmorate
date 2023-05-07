package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Film {
    @Nullable
    int id;
    @NotBlank String name;
    String description;
    LocalDate releaseDate;
    @Positive
    int duration;
    Rating mpa;
    Set<Genre> genres;
}

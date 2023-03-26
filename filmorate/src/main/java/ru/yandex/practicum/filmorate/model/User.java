package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {

    @Nullable
    private int id;
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String login;
    @Nullable
    private String name;
    private LocalDate birthday;

}

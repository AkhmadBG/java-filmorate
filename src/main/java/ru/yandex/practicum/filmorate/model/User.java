package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
public class User {

    private int id;

    @Email
    private String email;

    @NotNull
    private String login;

    private String name;

    private LocalDate birthday;

}

package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
public class User {

    private Integer id;

    @Email
    private String email;

    @NotNull
    private String login;

    private String name;

    private LocalDate birthday;

    private Set<Integer> friendsId = new HashSet<>();

}

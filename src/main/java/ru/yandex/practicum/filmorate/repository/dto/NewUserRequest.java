package ru.yandex.practicum.filmorate.repository.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewUserRequest {

    private String email;

    private String login;

    private String name;

    private LocalDate birthday;

}
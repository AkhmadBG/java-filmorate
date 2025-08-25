package ru.yandex.practicum.filmorate.repository.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

@Data
public class NewFilmRequest {

    private String name;

    private String description;

    private LocalDate releaseDate;

    private int duration;

    private Mpa mpa;

    private Set<Genre> genres;

    private Set<Director> directors;

}
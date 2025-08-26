package ru.yandex.practicum.filmorate.repository.dto.film;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
public class NewFilmRequest {

    private String name;

    private String description;

    private LocalDate releaseDate;

    private int duration;

    private Mpa mpa;

    private LinkedHashSet<Genre> genres;

    private List<Director> directors;

}
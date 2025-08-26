package ru.yandex.practicum.filmorate.repository.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UpdateFilmRequest {

    private int id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private int duration;

    private Mpa mpa;

    private Set<Genre> genres;

    private Set<Director> directors;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasReleaseDate() {
        return !(releaseDate == null);
    }

    public boolean hasDuration() {
        return !(duration == 0);
    }

    public boolean hasMpa() {
        return !(mpa == null);
    }

    public boolean hasGenres() {
        return !(genres == null);
    }

    public boolean hasDirectors() {
        return !(directors == null);
    }

}
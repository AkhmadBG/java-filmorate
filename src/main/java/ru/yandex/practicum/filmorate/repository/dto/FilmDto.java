package ru.yandex.practicum.filmorate.repository.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class FilmDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    private String name;

    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate releaseDate;

    private int duration;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Integer> likeUserList;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private MpaDto mpa;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Genre> genres = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Director> directors = new HashSet<>();

}
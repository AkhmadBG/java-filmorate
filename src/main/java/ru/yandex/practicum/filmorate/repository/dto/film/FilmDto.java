package ru.yandex.practicum.filmorate.repository.dto.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.dto.mpa.MpaDto;

import java.time.LocalDate;
import java.util.*;

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
    private List<Integer> likeUserList;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private MpaDto mpa;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Director> directors = new ArrayList<>();

}
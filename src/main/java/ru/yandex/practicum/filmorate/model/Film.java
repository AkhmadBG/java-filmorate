package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private int id;

    private String name;

    @Size(max = 200)
    private String description;

    private LocalDate releaseDate;

    @Positive
    private int duration;

    @Builder.Default
    private List<Integer> likeUserList = new ArrayList<>();

    private Mpa mpa;

    @Builder.Default
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();

    @Builder.Default
    private List<Director> directors = new ArrayList<>();

}
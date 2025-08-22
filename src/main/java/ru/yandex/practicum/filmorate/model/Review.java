package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class Review {

    private int reviewId;

    private int filmId;

    private int userId;

    @NotBlank
    @Size(min = 1, max = 1000)
    private String content;

    private Boolean isPositive;

    private Integer useful;

    private Map<Long, Boolean> userReactions;


    public Review() {
        this.useful = 0;
        this.userReactions = new HashMap<>();
    }
}

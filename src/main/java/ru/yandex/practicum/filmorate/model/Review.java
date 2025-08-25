package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    private Integer reviewId;

    private Integer filmId;

    private Integer userId;

    private String content;

    private Boolean isPositive;

    private Integer useful;

    private Map<Long, Boolean> userReactions;

}

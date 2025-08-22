package ru.yandex.practicum.filmorate.repository.dto.review;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ReviewDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int reviewId;

    private int filmId;

    private int userId;

    private String content;

    private Boolean isPositive;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer useful = 0;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Map<Long, Boolean> userReactions;

}

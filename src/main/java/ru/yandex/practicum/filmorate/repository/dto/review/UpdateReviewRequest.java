package ru.yandex.practicum.filmorate.repository.dto.review;

import lombok.Data;

@Data
public class UpdateReviewRequest {

    private Integer reviewId;

    private String content;

    private Boolean isPositive;

}
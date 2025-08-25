package ru.yandex.practicum.filmorate.repository.dto.review;


import lombok.Data;

@Data
public class NewReviewRequest {

    private String content;

    private Boolean isPositive;

    private Integer userId;

    private Integer filmId;
}

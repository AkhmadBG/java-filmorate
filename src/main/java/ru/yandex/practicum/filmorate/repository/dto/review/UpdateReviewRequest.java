package ru.yandex.practicum.filmorate.repository.dto.review;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateReviewRequest {

    @NotNull(message = "Поле не может быть пустым")
    private int reviewId;


    private String content;


    private Boolean isPositive;
}

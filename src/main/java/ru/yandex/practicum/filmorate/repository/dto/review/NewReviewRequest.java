package ru.yandex.practicum.filmorate.repository.dto.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewReviewRequest {
    @NotBlank(message = "Отзыв не может быть пустым")
    private String content;

    @NotNull(message = "Поле не может быть пустым")
    private Boolean isPositive;


    @NotNull(message = "Поле не может быть пустым")
    private Long userId;

    @NotNull(message = "Поле не может быть пустым")
    private Long filmId;
}

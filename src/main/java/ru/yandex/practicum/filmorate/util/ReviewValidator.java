package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.repository.dto.review.NewReviewRequest;

@UtilityClass
public class ReviewValidator {

    public static void validator(NewReviewRequest request) {

        if (request == null) {
            throw new ValidationException("ReviewValidator: Отзыв не найден");
        }

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new ValidationException("ReviewValidator: Отзыв не может быть пустым");
        }

        if (request.getIsPositive() == null) {
            throw new ValidationException("ReviewValidator: Тип отзыва не может быть пустым");
        }

        if (request.getUserId() == null) {
            throw new ValidationException("Не указан пользователь или пользователь не найден");
        }
        if (request.getUserId() <= 0) {
            throw new NotFoundException("Id не может быть отрицательным числом");
        }

        if (request.getFilmId() == null) {
            throw new ValidationException("Не указан фильм или фильм не найден");
        }
        if (request.getFilmId() <= 0) {
            throw new NotFoundException("Id не может быть отрицательным числом");
        }
    }

}
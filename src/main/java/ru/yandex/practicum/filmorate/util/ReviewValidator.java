package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.repository.dto.review.NewReviewRequest;

@UtilityClass
public class ReviewValidator {

    public static void validator(NewReviewRequest request) {

        if (request == null) {
            throw new ValidationException("ReviewValidator: Отзыв не может быь пустым");
        }

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new ValidationException("ReviewValidator: Отзыв не может быть пустым");
        }

        if (request.getIsPositive() == null) {
            throw new ValidationException("ReviewValidator: Тип отзыва не может быть пустым");
        }

        if (request.getUserId() == null || request.getFilmId() == null) {
            throw new NotFoundException("ReviewValidator: Поле не может быть пустым");
        }

        if (request.getUserId() < 1 || request.getFilmId() < 1) {
            throw new NotFoundException("ReviewValidator: Пользлватель или фильм не найден");
        }
    }
}

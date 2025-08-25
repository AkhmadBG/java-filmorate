package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.repository.dto.film.NewFilmRequest;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.util.AppConstants.MAX_DESCRIPTION_LENGTH;
import static ru.yandex.practicum.filmorate.util.AppConstants.STARTING_DATE;

@UtilityClass
public final class FilmValidator {

    public static void validator(NewFilmRequest newFilmRequest) {
        if (newFilmRequest == null) {
            throw new ValidationException("FilmValidator: film не может быть null");
        }

        if ((newFilmRequest.getName() == null || newFilmRequest.getName().isBlank())) {
            throw new ValidationException("FilmValidator: название фильма не может быть пустым");
        }

        if (newFilmRequest.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("FilmValidator: максимальная длина описания — 200 символов");
        }

        if (newFilmRequest.getReleaseDate().isBefore(STARTING_DATE) || newFilmRequest.getReleaseDate().isAfter(LocalDate.now())) {
            throw new ValidationException("FilmValidator: дата релиза — не раньше 28 декабря 1895 года, и не может быть позже текущей даты");
        }

        if (newFilmRequest.getDuration() <= 0) {
            throw new ValidationException("FilmValidator: продолжительность фильма должна быть положительной");
        }
    }

}
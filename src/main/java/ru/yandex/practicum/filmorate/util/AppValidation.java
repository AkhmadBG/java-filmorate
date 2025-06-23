package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.util.AppConstants.MAX_DESCRIPTION_LENGTH;
import static ru.yandex.practicum.filmorate.util.AppConstants.STARTING_DATE;

@Slf4j
public class AppValidation {

    public static void filmValidation(Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            log.error("валидация не пройдена: название не может быть пустым");
            throw new ValidationException("название не может быть пустым");
        }

        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.error("валидация не пройдена: максимальная длина описания — 200 символов");
            throw new ValidationException("максимальная длина описания — 200 символов");
        }

        if (film.getReleaseDate().isBefore(STARTING_DATE)) {
            log.error("валидация не пройдена: дата релиза — не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("дата релиза — не может быть раньше 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
            log.error("валидация не пройдена: продолжительность фильма должна быть положительным числом");
            throw new ValidationException("продолжительность фильма должна быть положительным числом");
        }

    }

    public static void userValidation(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("валидация не пройдена: электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().matches(" ")) {
            log.error("валидация не пройдена: логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.info("валидация не пройдена: если имя пользователя не указано — в таком случае будет использован логин;");
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("валидация не пройдена: дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения не может быть в будущем");
        }

    }

}

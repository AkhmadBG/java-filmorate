package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.repository.dto.user.NewUserRequest;

import java.time.LocalDate;

@UtilityClass
public class UserValidator {

    public static void validator(NewUserRequest newUserRequest) {
        if (newUserRequest == null) {
            throw new ValidationException("UserValidator: user не может быть null");
        }

        if (newUserRequest.getLogin() == null || newUserRequest.getLogin().isBlank()) {
            throw new ValidationException("UserValidator: логин не может быть пустым и содержать пробелы");
        }

        if (newUserRequest.getEmail() == null || newUserRequest.getEmail().isBlank() || !newUserRequest.getEmail().contains("@")) {
            throw new ValidationException("UserValidator: электронная почта не может быть пустой и должна содержать символ @");
        }

        if (newUserRequest.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("UserValidator: дата рождения не может быть в будущем");
        }

    }

}
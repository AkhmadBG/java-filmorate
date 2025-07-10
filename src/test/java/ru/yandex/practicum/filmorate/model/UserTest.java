package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.util.AppValidation;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateValidUser() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setLogin("LoginTest");
        user.setEmail("test@test.ru");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertEquals("User(id=1, email=test@test.ru, login=LoginTest, name=Test, birthday=2000-01-01, friendsId=[])", user.toString());
        assertEquals(1, user.getId());
        assertEquals("Test", user.getName());
        assertEquals("LoginTest", user.getLogin());
        assertEquals("test@test.ru", user.getEmail());
        assertEquals(LocalDate.of(2000, 1, 1), user.getBirthday());
    }

    @Test
    void shouldThrowValidationExceptionIncorrectEmail() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setLogin("LoginTest");
        user.setEmail("test.ru");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertThatThrownBy(() -> AppValidation.userValidation(user))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("электронная почта не может быть пустой и должна содержать символ @");
    }

    @Test
    void shouldThrowValidationExceptionIncorrectLogin() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setLogin("  ");
        user.setEmail("test@test.ru");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertThatThrownBy(() -> AppValidation.userValidation(user))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("логин не может быть пустым и содержать пробелы");
    }

    @Test
    void shouldThrowValidationExceptionIncorrectBirthday() {
        User user = new User();
        user.setId(1);
        user.setName("Test");
        user.setLogin("LoginTest");
        user.setEmail("test@test.ru");
        user.setBirthday(LocalDate.of(3000, 1, 1));

        assertThatThrownBy(() -> AppValidation.userValidation(user))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("дата рождения не может быть в будущем");
    }

    @Test
    void shouldSetLoginInUserName() {
        User user = new User();
        user.setId(1);
        user.setName("");
        user.setLogin("LoginTest");
        user.setEmail("test@test.ru");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        AppValidation.userValidation(user);

        assertEquals("LoginTest", user.getName());
    }

}
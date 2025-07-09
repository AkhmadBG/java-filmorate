package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.util.AppValidation;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    @Test
    void shouldCreateValidFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName("test film");
        film.setDescription("description");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2000, 1, 1));

        assertEquals("Film(id=1, name=test film, description=description, releaseDate=2000-01-01, duration=100, userLikes=[])",
                film.toString());
        assertEquals(1, film.getId());
        assertEquals("test film", film.getName());
        assertEquals("description", film.getDescription());
        assertEquals(100, film.getDuration());
        assertEquals(LocalDate.of(2000, 1, 1), film.getReleaseDate());
    }

    @Test
    void shouldThrowValidationExceptionNameIsNull() {
        Film film = new Film();
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);

        assertThatThrownBy(() -> AppValidation.filmValidation(film))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("название не может быть пустым");
    }

    @Test
    void shouldThrowValidationExceptionDescriptionMoreThen200() {
        Film film = new Film();
        film.setName("test film");
        film.setDescription("description".repeat(200));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(100);

        assertThatThrownBy(() -> AppValidation.filmValidation(film))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("максимальная длина описания — 200 символов");
    }

    @Test
    void shouldThrowValidationExceptionReleaseDateIsBeforeStartingDate() {
        Film film = new Film();
        film.setName("test film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(100);

        assertThatThrownBy(() -> AppValidation.filmValidation(film))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("дата релиза — не может быть раньше 28 декабря 1895 года");
    }

    @Test
    void shouldThrowValidationExceptionDurationIsNegative() {
        Film film = new Film();
        film.setName("test film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-100);

        assertThatThrownBy(() -> AppValidation.filmValidation(film))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("продолжительность фильма должна быть положительным числом");
    }

}
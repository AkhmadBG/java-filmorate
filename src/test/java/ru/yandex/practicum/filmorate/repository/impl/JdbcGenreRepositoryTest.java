package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({JdbcGenreRepository.class, GenreRowMapper.class})
class JdbcGenreRepositoryTest {

    private final GenreRepository genreRepository;

    @Test
    void shouldGenreExists() {
        Genre genre1 = new Genre(1, "Комедия");
        Genre genre2 = new Genre(10, "Comedy");
        boolean isExists1 = genreRepository.genreExists(Set.of(genre1));
        boolean isExists2 = genreRepository.genreExists(Set.of(genre2));

        assertThat(isExists1)
                .isEqualTo(true);
        assertThat(isExists2)
                .isEqualTo(false);
    }

    @Test
    void shouldGetGenreById() {
        Genre genre1 = genreRepository.getGenreById(1);
        Genre genre2 = new Genre(1, "Комедия");

        assertThat(genre1)
                .isEqualTo(genre2);
    }

    @Test
    void shouldGetAllGenres() {
        Collection<Genre> allGenres = genreRepository.getAllGenres();

        AssertionsForInterfaceTypes.assertThat(allGenres)
                .isNotNull()
                .hasSize(6);
    }

}
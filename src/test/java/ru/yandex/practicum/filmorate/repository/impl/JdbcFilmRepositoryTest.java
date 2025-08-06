package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({
        JdbcFilmRepository.class,
        JdbcUserRepository.class,
        JdbcMpaRepository.class,
        JdbcGenreRepository.class,
        GenreRowMapper.class,
        MpaRowMapper.class
})
class JdbcFilmRepositoryTest {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    @BeforeEach
    public void init() {
        String query1 = "INSERT INTO users (user_id, email, login, name, birthday) VALUES (1, 'test1@test.ru', 'login1', 'name1', '2001-01-01');";
        String query2 = "INSERT INTO users (user_id, email, login, name, birthday) VALUES (2, 'test2@test.ru', 'login2', 'name2', '2002-02-02');";
        String query3 = "INSERT INTO users (user_id, email, login, name, birthday) VALUES (3, 'test3@test.ru', 'login3', 'name3', '2003-03-03');";

        jdbcTemplate.execute(query1);
        jdbcTemplate.execute(query2);
        jdbcTemplate.execute(query3);

        String query4 = "INSERT INTO films (film_id, name, description, release_date, duration, rating_id) " +
                "VALUES (1, 'test1', 'test1Description', '2001-01-01', 10, 1)";
        String query5 = "INSERT INTO films (film_id, name, description, release_date, duration, rating_id) " +
                "VALUES (2, 'test2', 'test2Description', '2002-02-02', 20, 2)";
        String query6 = "INSERT INTO films (film_id, name, description, release_date, duration, rating_id) " +
                "VALUES (3, 'test3', 'test3Description', '2003-03-03', 30, 3)";

        jdbcTemplate.execute(query4);
        jdbcTemplate.execute(query5);
        jdbcTemplate.execute(query6);
    }

    @Test
    void shouldCheckFilmExists() {
        boolean isExists1 = filmRepository.filmExists(1);
        boolean isExists2 = filmRepository.filmExists(100);

        assertThat(isExists1)
                .isEqualTo(true);
        assertThat(isExists2)
                .isEqualTo(false);
    }

    @Test
    void shouldGetFilmById() {
        Film film = filmRepository.getFilmById(1);

        assertThat(film)
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getLikeUserList)
                .containsExactly(1, "test1", "test1Description", LocalDate.of(2001, 1, 1), 10, Set.of());
    }

    @Test
    void shouldGetAllFilms() {
        List<Film> allFilms = filmRepository.allFilms();
        Film film1 = filmRepository.getFilmById(1);
        Film film2 = filmRepository.getFilmById(2);
        Film film3 = filmRepository.getFilmById(3);

        AssertionsForInterfaceTypes.assertThat(allFilms)
                .isNotNull()
                .hasSize(3)
                .containsExactly(film1, film2, film3);
    }

    @Test
    void shouldUpdateFilm() {
        Mpa mpa = mpaRepository.getMpaById(1);
        Genre genre1 = genreRepository.getGenreById(1);
        Genre genre2 = genreRepository.getGenreById(2);

        Film film1 = Film.builder()
                .id(1)
                .name("updateName")
                .description("updateDescription")
                .releaseDate(LocalDate.of(2011, 11, 11))
                .duration(90)
                .likeUserList(new HashSet<>())
                .mpa(mpa)
                .genres(Set.of(genre1, genre2))
                .build();

        filmRepository.updateFilm(film1);
        Film updatedFilm = filmRepository.getFilmById(1);

        assertThat(updatedFilm).isNotNull();
        assertThat(updatedFilm.getName()).isEqualTo("updateName");
        assertThat(updatedFilm.getDescription()).isEqualTo("updateDescription");
        assertThat(updatedFilm.getReleaseDate()).isEqualTo(LocalDate.of(2011, 11, 11));
        assertThat(updatedFilm.getDuration()).isEqualTo(90);

        assertThat(updatedFilm.getMpa()).isNotNull();
        assertThat(updatedFilm.getMpa().getId()).isEqualTo(1);
        assertThat(updatedFilm.getMpa().getName()).isEqualTo(mpa.getName());

        AssertionsForInterfaceTypes.assertThat(updatedFilm.getGenres())
                .isNotNull()
                .hasSize(2)
                .extracting(Genre::getId)
                .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    void shouldAddFilm() {
        Mpa mpa = mpaRepository.getMpaById(1);
        Genre genre1 = genreRepository.getGenreById(1);
        Genre genre2 = genreRepository.getGenreById(2);

        String query = "DELETE FROM films WHERE film_id = 1";
        jdbcTemplate.execute(query);

        Film film4 = new Film();
        film4.setName("name4");
        film4.setDescription("description4");
        film4.setReleaseDate(LocalDate.of(2002, 2, 2));
        film4.setDuration(60);
        film4.setMpa(mpa);
        film4.setGenres(Set.of(genre1, genre2));

        filmRepository.addFilm(film4);

        assertThat(filmRepository.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration)
                .containsExactly(1, "name4", "description4", LocalDate.of(2002, 2, 2), 60);
    }

    @Test
    void shouldAddLike() {
        User user = userRepository.getUserById(1);
        Film film = filmRepository.getFilmById(1);
        filmRepository.addLike(film.getId(), user.getId());
        film = filmRepository.getFilmById(1);

        AssertionsForInterfaceTypes.assertThat(film.getLikeUserList())
                .containsExactlyInAnyOrder(user.getId());
    }

    @Test
    void shouldRemoveLike() {
        User user = userRepository.getUserById(1);
        Film film = filmRepository.getFilmById(1);
        filmRepository.removeLike(film.getId(), user.getId());
        film = filmRepository.getFilmById(1);

        AssertionsForInterfaceTypes.assertThat(film.getLikeUserList())
                .isEmpty();
    }

    @Test
    void shouldGetTopPopular() {
        filmRepository.addLike(1, 1);
        filmRepository.addLike(1, 2);
        filmRepository.addLike(1, 3);
        filmRepository.addLike(2, 1);
        filmRepository.addLike(2, 2);
        filmRepository.addLike(3, 1);

        List<Film> topPopular = filmRepository.getTopPopular(3);

        Film film1 = filmRepository.getFilmById(1);
        Film film2 = filmRepository.getFilmById(2);
        Film film3 = filmRepository.getFilmById(3);

        AssertionsForInterfaceTypes.assertThat(topPopular)
                .isNotNull()
                .containsExactly(film1, film2, film3);
    }

}
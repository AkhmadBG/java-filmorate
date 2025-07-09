package ru.yandex.practicum.filmorate.storage.impl;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class InMemoryFilmStorageTest {

    private InMemoryFilmStorage inMemoryFilmStorage;
    private InMemoryUserStorage inMemoryUserStorage;

    @BeforeEach
    public void init() {
        inMemoryUserStorage = new InMemoryUserStorage();
        inMemoryFilmStorage = new InMemoryFilmStorage(inMemoryUserStorage);
    }

    @Test
    void getAllFilms() {
        Set<Integer> likeUserList1 = new HashSet<>();
        Film film1 = new Film();
        film1.setUserLikes(likeUserList1);
        film1.setDuration(120);
        film1.setDescription("description1");
        film1.setName("name1");
        film1.setReleaseDate(LocalDate.of(2001, 1, 1));
        film1.setId(1);
        inMemoryFilmStorage.addFilm(film1);

        Set<Integer> likeUserList2 = new HashSet<>();
        Film film2 = new Film();
        film2.setUserLikes(likeUserList2);
        film2.setDuration(220);
        film2.setDescription("description2");
        film2.setName("name2");
        film2.setReleaseDate(LocalDate.of(2002, 2, 2));
        film2.setId(2);
        inMemoryFilmStorage.addFilm(film2);

        AssertionsForInterfaceTypes.assertThat(inMemoryFilmStorage.getAllFilms())
                .isNotNull()
                .hasSize(2)
                .containsExactly(film1, film2);
    }

    @Test
    void addFilm() {
        Set<Integer> likeUserList1 = new HashSet<>();
        Film film1 = new Film();
        film1.setUserLikes(likeUserList1);
        film1.setDuration(120);
        film1.setDescription("description1");
        film1.setName("name1");
        film1.setReleaseDate(LocalDate.of(2001, 1, 1));
        film1.setId(1);
        inMemoryFilmStorage.addFilm(film1);

        assertThat(inMemoryFilmStorage.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getUserLikes)
                .containsExactly(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of());
    }

    @Test
    void updateFilm() {
        Set<Integer> likeUserList1 = new HashSet<>();
        Film film1 = new Film();
        film1.setUserLikes(likeUserList1);
        film1.setDuration(120);
        film1.setDescription("description1");
        film1.setName("name1");
        film1.setReleaseDate(LocalDate.of(2001, 1, 1));
        film1.setId(1);
        inMemoryFilmStorage.addFilm(film1);

        Set<Integer> likeUserList2 = new HashSet<>();
        Film film2 = new Film();
        film2.setUserLikes(likeUserList2);
        film2.setDuration(220);
        film2.setDescription("description2");
        film2.setName("name2");
        film2.setReleaseDate(LocalDate.of(2002, 2, 2));
        film2.setId(1);
        inMemoryFilmStorage.updateFilm(film2);

        assertThat(inMemoryFilmStorage.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getUserLikes)
                .containsExactly(1, "name2", "description2", LocalDate.of(2002, 2, 2), 220, Set.of());

    }

    @Test
    void addLike() {
        Set<Integer> likeUserList1 = new HashSet<>();
        Film film1 = new Film();
        film1.setUserLikes(likeUserList1);
        film1.setDuration(120);
        film1.setDescription("description1");
        film1.setName("name1");
        film1.setReleaseDate(LocalDate.of(2001, 1, 1));
        film1.setId(1);
        inMemoryFilmStorage.addFilm(film1);

        Set<Integer> friendsList1 = Set.of(1, 2, 3);
        User user1 = new User();
        user1.setEmail("test1@test.ru");
        user1.setBirthday(LocalDate.of(2001, 1, 1));
        user1.setLogin("login1");
        user1.setName("name1");
        user1.setId(1);
        user1.setFriendsId(friendsList1);
        inMemoryUserStorage.addUser(user1);

        inMemoryFilmStorage.addLike(film1.getId(), user1.getId());

        assertThat(inMemoryFilmStorage.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getUserLikes)
                .containsExactly(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of(user1.getId()));
    }

    @Test
    void deleteLike() {
        Set<Integer> likeUserList1 = new HashSet<>();
        Film film1 = new Film();
        film1.setUserLikes(likeUserList1);
        film1.setDuration(120);
        film1.setDescription("description1");
        film1.setName("name1");
        film1.setReleaseDate(LocalDate.of(2001, 1, 1));
        film1.setId(1);
        inMemoryFilmStorage.addFilm(film1);

        Set<Integer> friendsList1 = Set.of(1, 2, 3);
        User user1 = new User();
        user1.setEmail("test1@test.ru");
        user1.setBirthday(LocalDate.of(2001, 1, 1));
        user1.setLogin("login1");
        user1.setName("name1");
        user1.setId(1);
        user1.setFriendsId(friendsList1);
        inMemoryUserStorage.addUser(user1);

        inMemoryFilmStorage.addLike(film1.getId(), user1.getId());
        inMemoryFilmStorage.deleteLike(film1.getId(), user1.getId());

        assertThat(inMemoryFilmStorage.getFilmById(1))
                .isNotNull()
                .extracting(Film::getId, Film::getName, Film::getDescription, Film::getReleaseDate, Film::getDuration, Film::getUserLikes)
                .containsExactly(1, "name1", "description1", LocalDate.of(2001, 1, 1), 120, Set.of());
    }

}
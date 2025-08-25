package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository {

    boolean filmExists(int filmId);

    Film getFilmById(int filmId);

    Film addFilm(Film film);

    void updateFilm(Film film);

    List<Film> allFilms();

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> getTopPopular(int count);

    void deleteFilm(int filmId);

    List<Film> getCommonFilms(int userId, int friendId);

    List<Film> getFilmsByDirector(int directorId, FilmSortBy sortBy);

}
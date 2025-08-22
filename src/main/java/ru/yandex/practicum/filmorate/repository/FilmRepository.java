package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UsersLikesFilms;

import java.util.List;
import java.util.Map;

public interface FilmRepository {

    boolean filmExists(int filmId);

    Film getFilmById(int filmId);

    Film addFilm(Film film);

    void updateFilm(Film film);

    List<Film> allFilms();

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> getTopPopular(int count);

}
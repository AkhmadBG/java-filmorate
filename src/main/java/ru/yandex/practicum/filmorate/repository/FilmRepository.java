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

    /**
     * Находит фильмы, которые лайкнул один пользователь, но не лайкнул другой
     * @param sourceUserId ID пользователя который лайкнул фильмы
     * @param targetUserId ID пользователя который не лайкнул фильмы
     * @return Список рекомендованных фильмов
     */
    List<Film> getRecommendedFilms(int sourceUserId, int targetUserId);
}
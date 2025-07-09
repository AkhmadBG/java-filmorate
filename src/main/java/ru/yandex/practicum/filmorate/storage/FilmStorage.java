package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {

    Film getFilmById(int filmId);

    Collection<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film addLike(int filmId, int userId);

    Film deleteLike(int filmId, int userId);

    Set<Film> popularFilms(int count);

}

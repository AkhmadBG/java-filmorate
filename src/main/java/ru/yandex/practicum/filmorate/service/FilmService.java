package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.FilmSortBy;
import ru.yandex.practicum.filmorate.repository.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.repository.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.repository.dto.film.UpdateFilmRequest;

import java.util.List;
import java.util.Set;

public interface FilmService {

    FilmDto addFilm(NewFilmRequest newFilmRequest);

    List<FilmDto> allFilms();

    FilmDto updateFilm(UpdateFilmRequest updateFilmRequest);

    FilmDto getFilmById(int filmId);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    Set<FilmDto> getTopFilms(int count, Integer genreId, Integer year);

    void deleteFilm(int filmId);

    List<FilmDto> getCommonFilms(int userId, int friendId);

    List<FilmDto> searchFilms(String query, String by);

    List<FilmDto> getFilmsByDirector(int directorId, FilmSortBy sortBy);

}
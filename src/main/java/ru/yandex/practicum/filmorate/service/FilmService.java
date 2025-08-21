package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.repository.dto.FilmDto;
import ru.yandex.practicum.filmorate.repository.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.repository.dto.UpdateFilmRequest;

import java.util.List;
import java.util.Set;

public interface FilmService {

    FilmDto addFilm(NewFilmRequest newFilmRequest);

    List<FilmDto> allFilms();

    FilmDto updateFilm(UpdateFilmRequest updateFilmRequest);

    FilmDto getFilmById(int filmId);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    Set<FilmDto> getTopFilms(int count);

    List<FilmDto> searchFilms(String query, String by);
}
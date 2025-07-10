package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.util.AppValidation;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;

    @Override
    public Film getFilmById(int filmId) {
        return filmStorage.getFilmById(filmId);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film addFilm(Film film) {
        log.debug("Начало валидации параметров фильма при добавлении нового");
        AppValidation.filmValidation(film);
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        if (!filmStorage.getAllFilms().contains(film)) {
            log.error("Фильм не существует");
            throw new NotFoundException("фильм не существует");
        }
        log.debug("Начало валидации параметров фильма при обновлении существующего");
        AppValidation.filmValidation(film);
        return filmStorage.updateFilm(film);
    }

    @Override
    public Film addLike(int filmId, int userId) {
        log.debug("Пользователь с id {} поставил like фильму с id {}", userId, filmId);
        return filmStorage.addLike(filmId, userId);
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        log.debug("Пользователь с id {} удалил like у фильма с id {}", userId, filmId);
        return filmStorage.deleteLike(filmId, userId);
    }

    @Override
    public Collection<Film> popularFilms(int count) {
        log.debug("Запрошен топ-{} фильмов", count);
        return filmStorage.popularFilms(count);
    }

}

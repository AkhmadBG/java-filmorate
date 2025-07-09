package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int identifier = 0;
    private final UserStorage userStorage;

    @Override
    public Film getFilmById(int filmId) {
        log.info("Запрос фильма с id {} из FilmStorageImpl", filmId);
        if (!films.containsKey(filmId)) {
            log.error("Фильм с id " + filmId + " не найден");
            throw new NotFoundException("фильм с id " + filmId + " не найден");
        }
        return films.get(filmId);
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Получение списка фильмов из FilmStorageImpl");
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(++identifier);
        films.put(film.getId(), film);
        log.info("Новый фильм с id {} добавлен в FilmStorageImpl", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        log.info("Фильм с id {} обновлен в FilmStorageImpl", film.getId());
        return film;
    }

    @Override
    public Film addLike(int filmId, int userId) {
        if (!films.containsKey(filmId)) {
            log.error("Фильм с id " + filmId + " не найден");
            throw new NotFoundException("фильм с id " + filmId + " не найден");
        }
        if (userStorage.getUserById(userId) == null) {
            log.error("Пользователь с id " + userId + " не найден");
            throw new NotFoundException("пользователь с id " + userId + " не найден");
        }
        Film film = films.get(filmId);
        film.getUserLikes().add(userId);
        log.debug("Пользователь с id {} поставил like фильму с id {} в FilmStorageImpl", userId, filmId);
        return films.get(filmId);
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        if (!films.containsKey(filmId)) {
            log.error("Фильм с id " + filmId + " не найден");
            throw new NotFoundException("фильм с id " + filmId + " не найден");
        }
        if (userStorage.getUserById(userId) == null) {
            log.error("Пользователь с id " + userId + " не найден");
            throw new NotFoundException("пользователь с id " + userId + " не найден");
        }
        Film film = films.get(filmId);
        film.getUserLikes().remove(userId);
        log.debug("Пользователь с id {} удалил like у фильма с id {} в FilmStorageImpl", userId, filmId);
        return films.get(filmId);
    }

    @Override
    public Set<Film> popularFilms(int count) {
        if (count <= 0) {
            log.error("Значение count не может быть отрицательным или равно 0");
            throw new IllegalArgumentException("значение count не может быть отрицательным или равно 0");
        }
        if (films.size() <= count) {
            log.debug("Запрошен топ-{} фильмов в FilmStorageImpl", count);
            return films.values().stream()
                    .sorted(Comparator.comparingInt((Film f) -> f.getUserLikes().size()).reversed())
                    .collect(Collectors.toSet());
        }
        log.debug("Запрошен топ-{} фильмов в FilmStorageImpl", count);
        return films.values().stream()
                .sorted(Comparator.comparingInt((Film f) ->
                        f.getUserLikes() == null ? 0 : f.getUserLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toSet());
    }

}

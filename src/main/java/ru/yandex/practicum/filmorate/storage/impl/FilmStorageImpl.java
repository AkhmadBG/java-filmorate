package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FilmStorageImpl implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int identifier = 0;

    @Override
    public Collection<Film> getAllFilms() {
        log.info("получение списка фильмов из FilmStorageImpl");
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(++identifier);
        films.put(film.getId(), film);
        log.info("новый фильм с id {} добавлен в FilmStorageImpl", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        log.info("фильм с id {} обновлен в FilmStorageImpl", film.getId());
        return film;
    }

}

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
    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film addFilm(Film film) {
        log.debug("начало валидации параметров фильма при добавлении нового");
        AppValidation.filmValidation(film);
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        if (!filmStorage.getAllFilms().contains(film)) {
            log.error("фильм не существует");
            throw new NotFoundException("фильм не существует");
        }
        log.debug("начало валидации параметров фильма при обновлении существующего");
        AppValidation.filmValidation(film);
        return filmStorage.updateFilm(film);
    }

}

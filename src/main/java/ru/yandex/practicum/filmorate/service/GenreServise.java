package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface GenreServise {

    Genre getGenreById(int genreId);

    Collection<Genre> getAllGenres();

}

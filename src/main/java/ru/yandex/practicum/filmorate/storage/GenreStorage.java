package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface GenreStorage {



    Genre getGenreById(int genreId);

    Collection<Genre> getAllGenres();

}

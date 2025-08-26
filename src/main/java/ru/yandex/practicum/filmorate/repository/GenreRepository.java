package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public interface GenreRepository {

    boolean genreExists(LinkedHashSet<Genre> genres);

    Genre getGenreById(int genreId);

    Collection<Genre> getAllGenres();

}
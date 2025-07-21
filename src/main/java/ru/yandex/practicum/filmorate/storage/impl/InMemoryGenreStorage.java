package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.Map;

@Slf4j
@Component
public class InMemoryGenreStorage implements GenreStorage {

    private final Map<Integer, Genre> genres = Map.of(1, new Genre(1, "Комедия"),
            2, new Genre(2, "Драма"),
            3, new Genre(3, "Мультфильм"),
            4, new Genre(4, "Триллер"),
            5, new Genre(5, "Документальный"),
            6, new Genre(6, "Боевик"));

    @Override
    public Genre getGenreById(int genreId) {
        return genres.get(genreId);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return genres.values();
    }
}

package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreServise;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreServise {

    private final GenreStorage genreStorage;

    @Override
    public Genre getGenreById(int genreId) {
        return genreStorage.getGenreById(genreId);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

}

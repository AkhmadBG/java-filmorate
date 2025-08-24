package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.dto.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreServise;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreServise {

    private final GenreRepository genreRepository;

    @Override
    public GenreDto getGenreById(int genreId) {
        log.info("GenreServiceImpl: запрошен жанр с id {} ", genreId);
        Genre genre = genreRepository.getGenreById(genreId);
        return GenreMapper.mapToGenreDto(genre);
    }

    @Override
    public Collection<GenreDto> getAllGenres() {
        log.info("GenreServiceImpl: запрошен список всех жанров, всего жанров {}", genreRepository.getAllGenres().size());
        return genreRepository.getAllGenres().stream()
                .map(GenreMapper::mapToGenreDto)
                .collect(Collectors.toList());
    }

}
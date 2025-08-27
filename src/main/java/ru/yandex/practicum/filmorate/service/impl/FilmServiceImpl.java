package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.filmMap.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmSortBy;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.repository.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.repository.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.util.FilmValidator;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    @Override
    public FilmDto addFilm(NewFilmRequest newFilmRequest) {
        FilmValidator.validator(newFilmRequest);
        Film film = filmRepository.addFilm(FilmMapper.mapToFilm(newFilmRequest));
        log.info("FilmServiceImpl: новый фильм {}, с id {} добавлен", film.getName(), film.getId());
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto updateFilm(UpdateFilmRequest updateFilmRequest) {
        Film film = filmRepository.getFilmById(updateFilmRequest.getId());
        FilmMapper.updateFilm(film, updateFilmRequest);
        filmRepository.updateFilm(film);
        log.info("FilmServiceImpl: фильм {}, с id {} обновлен", film.getName(), film.getId());
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public List<FilmDto> allFilms() {
        log.info("FilmServiceImpl: запрошен список всех фильмов, всего фильмов {}", filmRepository.allFilms().size());
        return filmRepository.allFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public FilmDto getFilmById(int filmId) {
        log.info("FilmServiceImpl: запрошен фильм, с id {} ", filmId);
        Film film = filmRepository.getFilmById(filmId);
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public void addLike(int filmId, int userId) {
        filmRepository.addLike(filmId, userId);
        log.info("FilmServiceImpl: Пользователю с id {} нравится фильм с id: {}", userId, filmId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        if (!filmRepository.filmExists(filmId)) {
            throw new NotFoundException("пользователь с id: " + userId + " не найден");
        }
        if (!userRepository.userExists(userId)) {
            throw new NotFoundException("пользователь с id: " + userId + " не найден");
        }
        filmRepository.removeLike(filmId, userId);
        log.info("FilmServiceImpl: Пользователю с id {} перестал нравится фильм с id: {}", userId, filmId);
    }

    @Override
    public Set<FilmDto> getTopFilms(int count, Integer genreId, Integer year) {
        log.info("FilmServiceImpl: запрос топ-" + count + " фильмов с параметрами (" + genreId + "," + year + " )");
        List<Film> topPopular = filmRepository.getTopPopular(count, genreId, year);
        return topPopular.stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public List<FilmDto> getCommonFilms(int userId, int friendId) {
        log.debug("FilmServiceImpl: запрос в сервис: userId={}, friendId={}", userId, friendId);
        List<Film> commonFilms = filmRepository.getCommonFilms(userId, friendId);
        return commonFilms.stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FilmDto> searchFilms(String query, String by) {
        List<Film> searchFilms = filmRepository.search(query, by);
        return searchFilms.stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FilmDto> getFilmsByDirector(int directorId, FilmSortBy sortBy) {
        log.info("FilmController: запрошен список фильмов режиссера с id = {} и отсортированный по {}", directorId, sortBy);
        List<Film> filmByDirector = filmRepository.getFilmsByDirector(directorId, sortBy);
        return filmByDirector.stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFilm(int filmId) {
        if (!filmRepository.filmExists(filmId)) {
            throw new NotFoundException("FilmServiceImpl: фильм с id: " + filmId + " не найден");
        }
        filmRepository.deleteFilm(filmId);
        log.info("FilmServiceImpl: фильм с id: {} удалён", filmId);
    }

}
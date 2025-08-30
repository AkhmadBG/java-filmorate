package ru.yandex.practicum.filmorate.mappers.filmMap;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.mappers.mpaMap.MpaMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.repository.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.repository.dto.film.UpdateFilmRequest;

import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {

    public static Film mapToFilm(NewFilmRequest newFilmRequest) {
        Film film = new Film();
        film.setName(newFilmRequest.getName());
        film.setDescription(newFilmRequest.getDescription());
        film.setReleaseDate(newFilmRequest.getReleaseDate());
        film.setDuration(newFilmRequest.getDuration());
        film.setMpa(newFilmRequest.getMpa());
        film.setGenres(newFilmRequest.getGenres());
        film.setDirectors(newFilmRequest.getDirectors());
        return film;
    }

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());
        filmDto.setLikeUserList(film.getLikeUserList());
        filmDto.setMpa(MpaMapper.mapToMpaDto(film.getMpa()));
        filmDto.setGenres(film.getGenres());
        filmDto.setDirectors(film.getDirectors());
        return filmDto;
    }

    public static void updateFilm(Film film, UpdateFilmRequest updateFilmRequest) {
        if (updateFilmRequest.hasName()) {
            film.setName(updateFilmRequest.getName());
        }
        if (updateFilmRequest.hasDescription()) {
            film.setDescription(updateFilmRequest.getDescription());
        }
        if (updateFilmRequest.hasReleaseDate()) {
            film.setReleaseDate(updateFilmRequest.getReleaseDate());
        }
        if (updateFilmRequest.hasDuration()) {
            film.setDuration(updateFilmRequest.getDuration());
        }

        if (updateFilmRequest.hasMpa()) {
            film.setMpa(updateFilmRequest.getMpa());
        }

        if (updateFilmRequest.hasGenres()) {
            film.setGenres(updateFilmRequest.getGenres());
        }

        if (updateFilmRequest.hasDirectors()) {
            film.setDirectors(updateFilmRequest.getDirectors());
        } else if (updateFilmRequest.getDirectors() == null) {
            film.setDirectors(new ArrayList<>());
        }
    }

}
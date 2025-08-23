package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.repository.dto.FilmDto;
import ru.yandex.practicum.filmorate.repository.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.repository.dto.UpdateFilmRequest;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<FilmDto> addFilm(@RequestBody NewFilmRequest newFilmRequest) {
        FilmDto filmDto = filmService.addFilm(newFilmRequest);
        log.info("FilmController: добавлен новый фильм: {}", filmDto.getId());
        return ResponseEntity.ok(filmDto);
    }

    @PutMapping()
    public ResponseEntity<FilmDto> updateFilm(@RequestBody UpdateFilmRequest updateFilmRequest) {
        FilmDto filmDto = filmService.updateFilm(updateFilmRequest);
        log.info("FilmController: фильм обновлен: {}", updateFilmRequest.getId());
        return ResponseEntity.ok(filmDto);
    }

    @GetMapping
    public ResponseEntity<List<FilmDto>> allFilms() {
        log.info("FilmController: количество всех фильмов: {}", filmService.allFilms().size());
        return ResponseEntity.ok(filmService.allFilms());
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<FilmDto> getFilm(@PathVariable(value = "filmId") int filmId) {
        log.info("FilmController: запрошен фильм с id: {}", filmId);
        return ResponseEntity.ok(filmService.getFilmById(filmId));
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable int filmId, @PathVariable int userId) {
        filmService.addLike(filmId, userId);
        log.info("FilmController: пользователю с id {} понравится фильм с id: {}", userId, filmId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable(value = "filmId") int filmId,
                                           @PathVariable(value = "userId") int userId) {
        filmService.removeLike(filmId, userId);
        log.info("FilmController: пользователю с id {} перестал нравится фильм с id: {}", userId, filmId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<Set<FilmDto>> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("FilmController: запрошен топ {} фильмов", count);
        return ResponseEntity.ok(filmService.getTopFilms(count));
    }

    @DeleteMapping("/{filmId}")
    public ResponseEntity<Void> deleteFilm(@PathVariable int filmId) {
        filmService.deleteFilm(filmId);
        log.info("FilmController: фильм с id: {} удалён", filmId);
        return ResponseEntity.noContent().build();
    }
}
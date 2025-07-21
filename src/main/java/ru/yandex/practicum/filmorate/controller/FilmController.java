package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        log.info("Запрошен список всех фильмов");
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        log.info("Запрос на добавление нового фильма");
        filmService.addFilm(film);
        return ResponseEntity.ok(film);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        log.info("Запрос на обновление фильма с id {}", film.getId());
        filmService.updateFilm(film);
        return ResponseEntity.ok(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable int id,
                                        @PathVariable int userId) {
        log.info("Запрос от пользователя с id {} на like фильму с id {}", userId, id);
        return ResponseEntity.ok(filmService.addLike(id, userId));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> deleteLike(@PathVariable int id,
                                           @PathVariable int userId) {
        log.info("Запрос от пользователя с id {} на удаление like фильму с id {}", userId, id);
        return ResponseEntity.ok(filmService.deleteLike(id, userId));
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<Film>> popularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Запрошен топ-{} фильмов", count);
        return ResponseEntity.ok(filmService.popularFilms(count));
    }

}

package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreServise;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreServise genreServise;

    @GetMapping
    public ResponseEntity<Collection<Genre>> getAllGenre() {
        return ResponseEntity.ok(genreServise.getAllGenres());
    }

    @GetMapping("/{genreId}")
    public ResponseEntity<Genre> getGenreById(@PathVariable int genreId) {
        return ResponseEntity.ok(genreServise.getGenreById(genreId));
    }

}

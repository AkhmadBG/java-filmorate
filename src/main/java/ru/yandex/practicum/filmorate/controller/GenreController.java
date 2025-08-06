package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.GenreServise;
import ru.yandex.practicum.filmorate.repository.dto.GenreDto;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreServise genreServise;

    @GetMapping
    public ResponseEntity<Collection<GenreDto>> getAllGenre() {
        Collection<GenreDto> allGenres = genreServise.getAllGenres();
        log.info("GenreController: количество всех жанров: {}", allGenres.size());
        return ResponseEntity.ok(allGenres);
    }

    @GetMapping("/{genreId}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable int genreId) {
        GenreDto genreDto = genreServise.getGenreById(genreId);
        log.info("GenreController: запрошен жанр с id: {}", genreDto.getId());
        return ResponseEntity.ok(genreDto);
    }

}
package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaServise;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final MpaServise mpaServise;

    @GetMapping
    public ResponseEntity<Collection<Mpa>> getAllMpa() {
        return ResponseEntity.ok(mpaServise.getAllMpa());
    }

    @GetMapping("/{mpaId}")
    public ResponseEntity<Mpa> getMpaById(@PathVariable int mpaId) {
        return ResponseEntity.ok(mpaServise.getMpaById(mpaId));
    }

}

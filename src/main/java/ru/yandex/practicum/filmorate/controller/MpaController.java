package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.repository.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.service.MpaServise;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final MpaServise mpaServise;

    @GetMapping
    public ResponseEntity<Collection<MpaDto>> getAllMpa() {
        Collection<MpaDto> allMpa = mpaServise.getAllMpa();
        log.info("MpaController: количество всех рейтингов: {}", allMpa.size());
        return ResponseEntity.ok(allMpa);
    }

    @GetMapping("/{mpaId}")
    public ResponseEntity<MpaDto> getMpaById(@PathVariable int mpaId) {
        MpaDto mpaDto = mpaServise.getMpaById(mpaId);
        log.info("MpaController: запрошен рейтинг с id: {}", mpaDto.getId());
        return ResponseEntity.ok(mpaDto);
    }

}
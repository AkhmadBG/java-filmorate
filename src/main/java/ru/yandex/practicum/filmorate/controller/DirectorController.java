package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.repository.dto.DirectorDto;
import ru.yandex.practicum.filmorate.repository.dto.NewDirectorRequest;
import ru.yandex.practicum.filmorate.repository.dto.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping("/{directorId}")
    public ResponseEntity<DirectorDto> getDirector(@PathVariable(value = "directorId") int directorId) {
        log.info("DirectorController: запрошен режиссер с id: {}", directorId);
        return ResponseEntity.ok(directorService.getDirectorById(directorId));
    }

    @GetMapping()
    public ResponseEntity<List<DirectorDto>> allDirectors() {
        List<DirectorDto> allDirectors = directorService.allDirectors();
        log.info("DirectorController: количество всех режиссеров: {}", allDirectors.size());
        return ResponseEntity.ok(allDirectors);
    }

    @PostMapping()
    public ResponseEntity<DirectorDto> addDirector(@RequestBody NewDirectorRequest newDirectorRequest) {
        DirectorDto directorDto = directorService.addDirector(newDirectorRequest);
        log.info("DirectorController: добавлен новый режиссер: {}", directorDto.getId());
        return ResponseEntity.ok(directorDto);
    }

    @PutMapping()
    public ResponseEntity<DirectorDto> updateDirector(@RequestBody UpdateDirectorRequest updateDirectorRequest) {
        DirectorDto directorDto = directorService.updateDirector(updateDirectorRequest);
        log.info("DirectorController: данные режиссера обновлены: {}", directorDto.getId());
        return ResponseEntity.ok(directorDto);
    }

    @DeleteMapping("/{directorId}")
    public ResponseEntity<Void> deleteDirector(@PathVariable(value = "directorId") int directorId) {
        directorService.deleteDirector(directorId);
        log.info("DirectorController: режиссер с id {} удален", directorId);
        return ResponseEntity.noContent().build();
    }

}

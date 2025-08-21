package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;
import ru.yandex.practicum.filmorate.repository.dto.DirectorDto;
import ru.yandex.practicum.filmorate.repository.dto.NewDirectorRequest;
import ru.yandex.practicum.filmorate.repository.dto.UpdateDirectorRequest;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;

    @Override
    public DirectorDto addDirector(NewDirectorRequest newDirectorRequest) {
        Director director = directorRepository.addDirector(DirectorMapper.mapToDirector(newDirectorRequest));
        log.info("DirectorServiceImpl: добавлен режиссер с id {} ", director.getId());
        return DirectorMapper.mapToDirectorDto(director);
    }

    @Override
    public DirectorDto updateDirector(UpdateDirectorRequest updateDirectorRequest) {
        Director director = directorRepository.getDirectorById(updateDirectorRequest.getId());
        if (director == null) {
            throw new NotFoundException("режиссер с id " + updateDirectorRequest.getId() + " не найден");
        }
        DirectorMapper.updateDirector(director, updateDirectorRequest);
        directorRepository.updateDirector(director);
        log.info("DirectorServiceImpl: данные режиссера с id {} обновлены ", director.getId());
        return DirectorMapper.mapToDirectorDto(director);
    }

    @Override
    public DirectorDto getDirectorById(int directorId) {
        log.info("DirectorServiceImpl: запрошен режиссер с id {} ", directorId);
        Director director = directorRepository.getDirectorById(directorId);
        return DirectorMapper.mapToDirectorDto(director);
    }

    @Override
    public List<DirectorDto> allDirectors() {
        List<Director> allDirectors = directorRepository.allDirectors();
        log.info("DirectorServiceImpl: запрошен список всех режиссеров, всего режиссеров {}", allDirectors.size());
        return allDirectors.stream()
                .map(DirectorMapper::mapToDirectorDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDirector(int directorId) {
        directorRepository.deleteDirector(directorId);
        log.info("DirectorServiceImpl: режиссер с id {} удален", directorId);
    }
}
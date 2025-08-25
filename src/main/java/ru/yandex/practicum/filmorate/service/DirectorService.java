package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.repository.dto.DirectorDto;
import ru.yandex.practicum.filmorate.repository.dto.NewDirectorRequest;
import ru.yandex.practicum.filmorate.repository.dto.UpdateDirectorRequest;

import java.util.List;

public interface DirectorService {

    DirectorDto addDirector(NewDirectorRequest newDirectorRequest);

    DirectorDto updateDirector(UpdateDirectorRequest updateDirectorRequest);

    DirectorDto getDirectorById(int directorId);

    List<DirectorDto> allDirectors();

    void deleteDirector(int directorId);

}
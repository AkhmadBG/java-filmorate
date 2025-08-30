package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.repository.dto.director.DirectorDto;
import ru.yandex.practicum.filmorate.repository.dto.director.NewDirectorRequest;
import ru.yandex.practicum.filmorate.repository.dto.director.UpdateDirectorRequest;

import java.util.List;

public interface DirectorService {

    DirectorDto addDirector(NewDirectorRequest newDirectorRequest);

    DirectorDto updateDirector(UpdateDirectorRequest updateDirectorRequest);

    DirectorDto getDirectorById(int directorId);

    List<DirectorDto> allDirectors();

    void deleteDirector(int directorId);

}
package ru.yandex.practicum.filmorate.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.dto.DirectorDto;
import ru.yandex.practicum.filmorate.repository.dto.NewDirectorRequest;
import ru.yandex.practicum.filmorate.repository.dto.UpdateDirectorRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DirectorMapper {

    public static Director mapToDirector(NewDirectorRequest newDirectorRequest) {
        Director director = new Director();
        director.setName(newDirectorRequest.getName());
        return director;
    }

    public static DirectorDto mapToDirectorDto(Director director) {
        DirectorDto directorDto = new DirectorDto();
        directorDto.setId(director.getId());
        directorDto.setName(director.getName());
        return directorDto;
    }

    public static void updateDirector(Director director, UpdateDirectorRequest updateDirectorRequest) {
        if (updateDirectorRequest.hasName()) {
            director.setName(updateDirectorRequest.getName());
        }
    }

}
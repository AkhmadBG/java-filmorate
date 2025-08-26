package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Set;

public interface DirectorRepository {

    boolean directorsExists(List<Director> directors);

    Director getDirectorById(int directorId);

    List<Director> allDirectors();

    Director addDirector(Director director);

    void deleteDirector(int directorId);

    void updateDirector(Director director);

}
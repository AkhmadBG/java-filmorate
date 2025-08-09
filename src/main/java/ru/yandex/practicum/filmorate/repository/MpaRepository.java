package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaRepository {

    boolean mpaExists(int mpaId);

    Mpa getMpaById(int mpaId);

    Collection<Mpa> getAllMpa();

}
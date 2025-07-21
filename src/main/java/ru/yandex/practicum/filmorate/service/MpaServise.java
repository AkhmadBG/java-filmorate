package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaServise {

    Mpa getMpaById(int filmId);

    Collection<Mpa> getAllMpa();

}
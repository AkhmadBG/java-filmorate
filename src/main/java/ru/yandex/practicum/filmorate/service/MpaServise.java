package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.repository.dto.mpa.MpaDto;

import java.util.Collection;

public interface MpaServise {

    MpaDto getMpaById(int filmId);

    Collection<MpaDto> getAllMpa();

}
package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaServise;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaServise {

    private final MpaStorage mpaStorage;

    @Override
    public Mpa getMpaById(int mpaId) {
        return mpaStorage.getMpaById(mpaId);
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

}

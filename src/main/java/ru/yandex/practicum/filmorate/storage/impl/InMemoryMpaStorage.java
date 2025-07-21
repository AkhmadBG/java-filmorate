package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryMpaStorage implements MpaStorage {

    private final Map<Integer, Mpa> mpa = Map.of(1, new Mpa(1, "G"),
            2, new Mpa(2, "PG"),
            3, new Mpa(3, "PG-13"),
            4, new Mpa(4, "R"),
            5, new Mpa(5, "NC-17"));

    @Override
    public Mpa getMpaById(int mpaId) {
        return mpa.get(mpaId);
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        return mpa.values().stream()
                .sorted(Comparator.comparing(Mpa::getId))
                .collect(Collectors.toList());
    }

}

package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mappers.mpaMap.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import ru.yandex.practicum.filmorate.repository.dto.mpa.MpaDto;

import ru.yandex.practicum.filmorate.service.MpaServise;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaServise {

    private final MpaRepository mpaRepository;

    @Override
    public MpaDto getMpaById(int mpaId) {
        log.info("MpaServiceImpl: запрошен рейтинг с id {} ", mpaId);
        Mpa mpa = mpaRepository.getMpaById(mpaId);
        return MpaMapper.mapToMpaDto(mpa);
    }

    @Override
    public Collection<MpaDto> getAllMpa() {
        log.info("MpaServiceImpl: запрошен список всех рейтингов, всего рейтингов {}", mpaRepository.getAllMpa().size());
        return mpaRepository.getAllMpa().stream()
                .map(MpaMapper::mapToMpaDto)
                .collect(Collectors.toList());
    }

}
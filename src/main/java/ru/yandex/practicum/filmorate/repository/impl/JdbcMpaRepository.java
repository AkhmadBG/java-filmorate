package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.mpaMap.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {

    private final NamedParameterJdbcOperations namedJdbc;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public boolean mpaExists(int mpaId) {
        String queryMpa = "SELECT COUNT(*) FROM rating_mpa WHERE rating_id = :rating_id";
        Map<String, Object> params = Map.of("rating_id", mpaId);
        Integer count = namedJdbc.queryForObject(queryMpa, params, Integer.class);
        log.info("MpaRepository: проверка существования рейтинга с id: {}", mpaId);
        return count == null || count > 0;
    }

    @Override
    public Mpa getMpaById(int mpaId) {
        String queryMpa = "SELECT * FROM rating_mpa WHERE rating_id = :rating_id";
        try {
            log.info("MpaRepository: запрос рейтинга с id: {}", mpaId);
            Map<String, Object> params = Map.of("rating_id", mpaId);
            return namedJdbc.queryForObject(queryMpa, params, mpaRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("рейтинг с id " + mpaId + " не найден");
        }
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        String query = "SELECT * FROM rating_mpa";
        List<Mpa> allMpa = namedJdbc.query(query, mpaRowMapper);
        log.info("MpaRepository: запрошен список всех рейтингов, всего рейтингов: {}", allMpa.size());
        return allMpa;
    }

}
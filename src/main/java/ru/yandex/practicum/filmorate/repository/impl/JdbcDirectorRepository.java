package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.directorMap.DirectorRowMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcDirectorRepository implements DirectorRepository {

    private final NamedParameterJdbcOperations namedJdbc;
    private final DirectorRowMapper directorRowMapper;

    @Override
    public boolean directorsExists(Set<Director> directors) {
        if (directors == null || directors.isEmpty()) {
            return true;
        }

        String sql = "SELECT director_id FROM directors WHERE director_id IN (:ids)";
        List<Integer> inputIds = directors.stream()
                .map(Director::getId)
                .collect(Collectors.toList());

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", inputIds);

        List<Integer> existingIds = namedJdbc.queryForList(sql, params, Integer.class);
        log.info("JdbcDirectorRepository: проверка существования жанра");
        return existingIds.size() == inputIds.size();
    }

    @Override
    public Director getDirectorById(int directorId) {
        String queryDirector = "SELECT * FROM directors WHERE director_id = :director_id";
        try {
            log.info("JdbcDirectorRepository: запрос режиссера с id: {}", directorId);
            Map<String, Object> params = Map.of("director_id", directorId);
            return namedJdbc.queryForObject(queryDirector, params, directorRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("режиссер с id " + directorId + " не найден");
        }
    }

    @Override
    public List<Director> allDirectors() {
        String query = "SELECT * FROM directors";
        List<Director> allDirector = namedJdbc.query(query, directorRowMapper);
        log.info("JdbcDirectorRepository: запрошен список всех режиссеров, всего режиссеров: {}", allDirector.size());
        return allDirector;
    }

    @Override
    public Director addDirector(Director director) {
        String query = "INSERT INTO directors (name) VALUES (:name)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", director.getName());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update(query, params, keyHolder, new String[]{"director_id"});

        Integer id = keyHolder.getKeyAs(Integer.class);
        if (id != null) {
            director.setId(id);
            log.info("JdbcDirectorRepository: добавлен новый режиссер с id: {}", director.getId());
            return director;
        } else {
            throw new RuntimeException("JdbcDirectorRepository: не удалось сохранить режиссера: id не сгенерирован");
        }
    }

    @Override
    public void updateDirector(Director director) {
        if (!directorsExists(Set.of(director))) {
            throw new NotFoundException("JdbcDirectorRepository: режиссер с id: " + director.getId() + " не найден");
        }
        try {
            String queryUpdateDirector = "UPDATE directors SET name = :name WHERE director_id = :director_id";
            Map<String, Object> params = Map.of("director_id", director.getId(),
                    "name", director.getName());
            namedJdbc.update(queryUpdateDirector, params);
            log.info("JdbcDirectorRepository: режиссер с id: {} обновлен", director.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("JdbcDirectorRepository: попытка обновления режиссера с id: " + director.getId() + " не удалась");
        }
    }

    @Override
    public void deleteDirector(int directorId) {
        String queryDeleteDirector = "DELETE FROM directors WHERE director_id = :director_id";
        Map<String, Object> params = Map.of("director_id", directorId);
        int rowsAffected = namedJdbc.update(queryDeleteDirector, params);
        if (rowsAffected == 0) {
            throw new NotFoundException("JdbcDirectorRepository: режиссер с id: " + directorId + " не найден");
        }
        log.info("JdbcDirectorRepository: режиссер с id: {} удален", directorId);
    }

}
package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.genreMap.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations namedJdbc;
    private final GenreRowMapper genreRowMapper;

    @Override
    public boolean genreExists(LinkedHashSet<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return true;
        }

        String sql = "SELECT genre_id FROM genres WHERE genre_id IN (:ids)";
        List<Integer> inputIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", inputIds);

        List<Integer> existingIds = namedJdbc.queryForList(sql, params, Integer.class);
        log.info("GenreRepository: проверка существования жанра");
        return existingIds.size() == inputIds.size();
    }

    @Override
    public Genre getGenreById(int genreId) {
        String query = "SELECT * FROM genres WHERE genre_id = :genre_id";
        try {
            log.info("GenreRepository: запрос жанра с id: {}", genreId);
            Map<String, Object> params = Map.of("genre_id", genreId);
            return namedJdbc.queryForObject(query, params, genreRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("GenreRepository: жанр с id " + genreId + " не найден");
        }
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String query = "SELECT * FROM genres";
        List<Genre> allGenres = namedJdbc.query(query, genreRowMapper);
        log.info("GenreRepository: запрошен список всех жанров, всего жанров: {}", allGenres.size());
        return allGenres;

    }

}
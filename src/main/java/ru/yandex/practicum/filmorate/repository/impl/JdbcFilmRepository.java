package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.CommonFilmsExtractor;
import ru.yandex.practicum.filmorate.mappers.FilmExtractor;
import ru.yandex.practicum.filmorate.mappers.FilmsExtractor;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {

    private final NamedParameterJdbcOperations namedJdbc;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;

    @Override
    public boolean filmExists(int filmId) {
        String sqlQuery = "SELECT COUNT(*) FROM films WHERE film_id = :film_id";
        Map<String, Object> params = Map.of("film_id", filmId);
        Integer count = namedJdbc.queryForObject(sqlQuery, params, Integer.class);
        log.info("FilmRepository: проверка существования фильма с id: {}", filmId);
        return count == null || count > 0;
    }

    @Override
    public Film getFilmById(int filmId) {
        String queryFilm = "SELECT f.film_id, " +
                "f.name AS film_name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "r.rating_id, " +
                "r.name AS rating_name, " +
                "l.user_id AS like_user_id, " +
                "g.genre_id, " +
                "g.name AS genre_name, " +
                "d.director_id, " +
                "d.name AS director_name " +
                "FROM films AS f " +
                "LEFT JOIN rating_mpa AS r ON f.rating_id = r.rating_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "LEFT JOIN films_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN films_directors AS fd ON f.film_id = fd.film_id " +
                "LEFT JOIN directors AS d ON fd.director_id = d.director_id " +
                "WHERE f.film_id = :film_id;";
        if (!filmExists(filmId)) {
            throw new NotFoundException("фильм с id: " + filmId + " не найден");
        }
        try {
            log.info("FilmRepository: запрос фильма с id: {}", filmId);
            Map<String, Object> params = Map.of("film_id", filmId);
            return namedJdbc.query(queryFilm, params, new FilmExtractor());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("FilmRepository: фильм с id: " + filmId + " не найден");
        }
    }

    @Override
    public List<Film> allFilms() {
        String queryFilms = "SELECT f.film_id, " +
                "f.name AS film_name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "r.rating_id, " +
                "r.name AS rating_name, " +
                "l.user_id AS like_user_id, " +
                "g.genre_id, " +
                "g.name AS genre_name, " +
                "d.director_id, " +
                "d.name AS director_name " +
                "FROM films AS f " +
                "LEFT JOIN rating_mpa AS r ON f.rating_id = r.rating_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "LEFT JOIN films_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN films_directors AS fd ON f.film_id = fd.film_id " +
                "LEFT JOIN directors AS d ON fd.director_id = d.director_id";
        List<Film> allFilms = namedJdbc.query(queryFilms, new FilmsExtractor());
        log.info("FilmRepository: запрошен список всех фильмов, количество зарегистрированных фильмов: {}", allFilms.size());
        return allFilms;
    }

    @Override
    public void updateFilm(Film film) {
        String queryUpdateFilm = "UPDATE films SET name = :name, description = :description, " +
                "release_date = :release_date, duration = :duration WHERE film_id = :film_id";

        Map<String, Object> params = Map.of(
                "film_id", film.getId(),
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate(),
                "duration", film.getDuration()
        );

        int rowsUpdated = namedJdbc.update(queryUpdateFilm, params);
        if (rowsUpdated == 0) {
            throw new NotFoundException("FilmRepository: фильм с id: " + film.getId() + " не найден");
        }

        log.info("FilmRepository: фильм с id: {} обновлен", film.getId());

        String queryDeleteFilmsGenres = "DELETE FROM films_genres WHERE film_id = :film_id";
        namedJdbc.update(queryDeleteFilmsGenres, Map.of("film_id", film.getId()));
        String queryInsertFilmsGenres = "INSERT INTO films_genres (film_id, genre_id) VALUES (:film_id, :genre_id)";
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                namedJdbc.update(queryInsertFilmsGenres, Map.of(
                        "film_id", film.getId(),
                        "genre_id", genre.getId()
                ));
            }
        }
        String queryDeleteFilmsDirectors = "DELETE FROM films_directors WHERE film_id = :film_id";
        namedJdbc.update(queryDeleteFilmsDirectors, Map.of("film_id", film.getId()));
        String queryInsertFilmsDirectors = "INSERT INTO films_directors (film_id, director_id) VALUES (:film_id, :director_id)";
        if (film.getDirectors() != null && !film.getDirectors().isEmpty()) {
            for (Director director : film.getDirectors()) {
                namedJdbc.update(queryInsertFilmsDirectors, Map.of(
                        "film_id", film.getId(),
                        "director_id", director.getId()
                ));
            }
        }
    }

    private void addGenresToFilm(int filmId, Set<Genre> genres) {
        String queryInsertFilmsGenres = "INSERT INTO films_genres (film_id, genre_id) VALUES (:film_id, :genre_id)";
        for (Genre genre : genres) {
            namedJdbc.update(queryInsertFilmsGenres, Map.of("film_id", filmId, "genre_id", genre.getId()));
            log.info("FilmRepository: к фильму с id: {} добавлен жанр с id: {}", filmId, genre.getId());
        }
    }

    private void addDirectorsToFilm(int filmId, Set<Director> directors) {
        String queryInsertFilmsDirectors = "INSERT INTO films_directors (film_id, director_id) VALUES (:film_id, :director_id)";
        for (Director director : directors) {
            namedJdbc.update(queryInsertFilmsDirectors, Map.of("film_id", filmId, "director_id", director.getId()));
            log.info("FilmRepository: к фильму с id: {} добавлен режиссер с id: {}", filmId, director.getId());
        }
    }

    @Override
    public Film addFilm(Film film) {
        String queryAddFilm = "INSERT INTO films (name, description, release_date, duration, rating_id) " +
                "VALUES (:name, :description, :release_date, :duration, :rating_id)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        Mpa mpa = film.getMpa();
        if (mpa == null || !mpaRepository.mpaExists(mpa.getId())) {
            throw new NotFoundException("рейтинг с id " + (mpa != null ? mpa.getId() : "null") + " не существует");
        }

        Set<Genre> genres = film.getGenres();
        if (genres != null && !genres.isEmpty() && !genreRepository.genreExists(genres)) {
            throw new NotFoundException("FilmRepository: один или несколько жанров не существуют");
        }

        Set<Director> directors = film.getDirectors();
        if (directors != null && !directors.isEmpty() && !directorRepository.directorsExists(directors)) {
            throw new NotFoundException("FilmRepository: один или несколько режиссеров не существуют");
        }

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("rating_id", mpa.getId());

        namedJdbc.update(queryAddFilm, params, keyHolder, new String[]{"film_id"});

        Integer id = keyHolder.getKeyAs(Integer.class);
        if (id == null) {
            throw new RuntimeException("FilmRepository: не удалось сохранить фильм: id не сгенерирован");
        }

        film.setId(id);

        if (genres != null && !genres.isEmpty()) {
            addGenresToFilm(id, genres);
        }

        if (directors != null && !directors.isEmpty()) {
            addDirectorsToFilm(id, directors);
        }

        log.info("FilmRepository: добавлен новый фильм с id: {}", film.getId());
        return film;
    }

    @Override
    public void addLike(int filmId, int userId) {
        String queryAddLike = "INSERT INTO likes (film_id, user_id) VALUES (:film_id, :user_id)";
        namedJdbc.update(queryAddLike, Map.of("film_id", filmId, "user_id", userId));
        log.info("FilmRepository: к фильму с id: {} добавлен like от пользователя с id: {}", filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String queryRemoveLike = "DELETE FROM likes WHERE film_id = :film_id AND user_id = :user_id";
        namedJdbc.update(queryRemoveLike, Map.of("film_id", filmId, "user_id", userId));
        log.info("FilmRepository: у фильма с id: {} удален like от пользователя с id: {}", filmId, userId);
    }

    @Override
    public List<Film> getTopPopular(int count) {
        String queryTopPopularFilms = "SELECT f.film_id, " +
                "f.name AS film_name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "r.rating_id, " +
                "r.name AS rating_name, " +
                "l.user_id AS like_user_id, " +
                "g.genre_id, " +
                "g.name AS genre_name, " +
                "d.director_id, " +
                "d.name AS director_name " +
                "FROM films AS f " +
                "LEFT JOIN rating_mpa AS r ON f.rating_id = r.rating_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "LEFT JOIN films_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN films_directors AS fd ON f.film_id = fd.film_id " +
                "LEFT JOIN directors AS d ON fd.director_id = d.director_id";
        List<Film> allFilms = namedJdbc.query(queryTopPopularFilms, new FilmsExtractor());
        if (allFilms == null) {
            throw new NotFoundException("FilmRepository: фильмы не найдены");
        }
        log.info("FilmRepository: запрошен топ-{} список фильмов", count);
        return allFilms.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt((Film film) ->
                        film.getLikeUserList() != null ? film.getLikeUserList().size() : 0).reversed())
                .limit(count)
                .collect(toList());
    }

    @Override
    public List<Film> getFilmsByDirector(int directorId, FilmSortBy sortBy) {
        String queryFilmsByDirector = "SELECT f.film_id, " +
                "f.name AS film_name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "r.rating_id, " +
                "r.name AS rating_name, " +
                "l.user_id AS like_user_id, " +
                "g.genre_id, " +
                "g.name AS genre_name, " +
                "d.director_id, " +
                "d.name AS director_name " +
                "FROM films AS f " +
                "LEFT JOIN rating_mpa AS r ON f.rating_id = r.rating_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "LEFT JOIN films_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN films_directors AS fd ON f.film_id = fd.film_id " +
                "LEFT JOIN directors AS d ON fd.director_id = d.director_id " +
                "WHERE d.director_id = :director_id";
        List<Film> filmsByDirector = namedJdbc.query(queryFilmsByDirector,
                Map.of("director_id", directorId),
                new FilmsExtractor());

        if (filmsByDirector.isEmpty()) {
            throw new NotFoundException("FilmRepository: фильмы режиссера с id " + directorId + " не найдены");
        }

        log.info("FilmRepository: запрошен список фильмов режиссера с id = {}", directorId);

        if (sortBy == FilmSortBy.LIKES) {
            return filmsByDirector.stream()
                    .sorted(Comparator
                            .comparing((Film film) -> film.getLikeUserList().size())
                            .reversed()
                            .thenComparing(Film::getId))
                    .collect(Collectors.toList());
        } else if (sortBy == FilmSortBy.YEAR) {
            return filmsByDirector.stream()
                    .sorted(Comparator
                            .comparing(Film::getReleaseDate)
                            .thenComparing(Film::getId))
                    .collect(Collectors.toList());
        } else {
            return filmsByDirector;
        }
    }

    @Override
    public List<Film> search(String query, String by) {
        String baseSql = "SELECT f.film_id, " +
                "f.name AS film_name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "r.rating_id, " +
                "r.name AS rating_name, " +
                "l.user_id AS like_user_id, " +
                "g.genre_id, " +
                "g.name AS genre_name, " +
                "d.director_id, " +
                "d.name AS director_name " +
                "FROM films AS f " +
                "LEFT JOIN rating_mpa AS r ON f.rating_id = r.rating_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "LEFT JOIN films_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN films_directors AS fd ON f.film_id = fd.film_id " +
                "LEFT JOIN directors AS d ON fd.director_id = d.director_id " +
                "WHERE ";


        boolean searchByTitle = by.contains("title");
        boolean searchByDirector = by.contains("director");

        StringBuilder sql = new StringBuilder(baseSql);

        if (searchByTitle && searchByDirector) {
            sql.append(" LOWER(f.name) LIKE :query OR LOWER(d.name) LIKE :query ");
        } else if (searchByTitle) {
            sql.append(" LOWER(f.name) LIKE :query ");
        } else if (searchByDirector) {
            sql.append(" LOWER(d.name) LIKE :query ");
        } else {
            return List.of();
        }

        sql.append(" GROUP BY f.film_id ");

        Map<String, Object> params = Map.of("query", "%" + query.toLowerCase() + "%");

        List<Film> films = namedJdbc.query(sql.toString(), params, new FilmsExtractor());
        return films.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt((Film film) ->
                        film.getLikeUserList() != null ? film.getLikeUserList().size() : 0).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        log.info("FilmRepository: поиск общих фильмов для userId={} и friendId={}", userId, friendId);
        String sql = "SELECT " +
                "f.film_id, " +
                "f.name AS film_name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "r.rating_id, " +
                "r.name AS rating_name, " +
                "g.genre_id, " +
                "g.name AS genre_name, " +
                "(SELECT COUNT(*) FROM likes WHERE film_id = f.film_id) AS likes_count " +
                "FROM films AS f " +
                "LEFT JOIN rating_mpa AS r ON f.rating_id = r.rating_id " +
                "LEFT JOIN films_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "WHERE f.film_id IN (" +
                "    SELECT l1.film_id " +
                "    FROM likes l1 " +
                "    JOIN likes l2 ON l1.film_id = l2.film_id " +
                "    WHERE l1.user_id = :userId AND l2.user_id = :friendId" +
                ") " +
                "ORDER BY likes_count DESC, f.film_id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("friendId", friendId);
        try {
            List<Film> films = namedJdbc.query(sql, params, new CommonFilmsExtractor());
            log.info("FilmRepository: найдено {} общих фильмов для userId={} и friendId={}",
                    films.size(), userId, friendId);
            return films;
        } catch (DataAccessException ex) {
            log.error("FilmRepository: ошибка при поиске общих фильмов для userId={} и friendId={}", userId, friendId, ex);
            throw new RuntimeException("Ошибка чтения из БД при поиске общих фильмов", ex);
        }
    }

    @Override
    public void deleteFilm(int filmId) {
        if (!filmExists(filmId)) {
            throw new NotFoundException("FilmRepository: фильм с id: " + filmId + " не найден");
        }
        String query = "DELETE FROM films WHERE film_id = :film_id";
        Map<String, Object> params = Map.of("film_id", filmId);
        namedJdbc.update(query, params);
        log.info("FilmRepository: фильм с id: {} удалён", filmId);
    }
}
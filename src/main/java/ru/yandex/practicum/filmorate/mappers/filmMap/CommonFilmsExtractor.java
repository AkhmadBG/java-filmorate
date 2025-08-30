package ru.yandex.practicum.filmorate.mappers.filmMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CommonFilmsExtractor implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Film> films = new LinkedHashMap<>();

        while (rs.next()) {
            int filmId = rs.getInt("film_id");
            Film film = films.get(filmId);

            if (film == null) {
                film = Film.builder()
                        .id(filmId)
                        .name(rs.getString("film_name"))
                        .description(rs.getString("description"))
                        .releaseDate(rs.getDate("release_date").toLocalDate())
                        .duration(rs.getInt("duration"))
                        .likeUserList(new ArrayList<>())
                        .mpa(new Mpa(rs.getInt("rating_id"), rs.getString("rating_name")))
                        .genres(new LinkedHashSet<>())
                        .build();
                films.put(filmId, film);
            }

            int genreId = rs.getInt("genre_id");
            if (!rs.wasNull()) {
                String genreName = rs.getString("genre_name");
                film.getGenres().add(new Genre(genreId, genreName));
            }
        }

        return new ArrayList<>(films.values());
    }

}
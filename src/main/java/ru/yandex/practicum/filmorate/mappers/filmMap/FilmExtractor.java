package ru.yandex.practicum.filmorate.mappers.filmMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FilmExtractor implements ResultSetExtractor<Film> {

    @Override
    public Film extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.next()) {
            return null;
        }

        Film film = Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .likeUserList(new ArrayList<>())
                .mpa(new Mpa(rs.getInt("rating_id"), rs.getString("rating_name")))
                .genres(new LinkedHashSet<>())
                .directors(new ArrayList<>())
                .build();

        List<Integer> likesUsersList = film.getLikeUserList();
        LinkedHashSet<Genre> genres = film.getGenres();
        List<Director> directors = film.getDirectors();

        int userId = rs.getInt("like_user_id");
        if (!rs.wasNull()) {
            likesUsersList.add(userId);
        }

        int genreId = rs.getInt("genre_id");
        if (!rs.wasNull()) {
            String genreName = rs.getString("genre_name");
            genres.add(new Genre(genreId, genreName));
        }

        int directorId = rs.getInt("director_id");
        if (!rs.wasNull()) {
            String directorName = rs.getString("director_name");
            directors.add(new Director(directorId, directorName));
        }

        while (rs.next()) {
            userId = rs.getInt("like_user_id");
            if (!rs.wasNull()) {
                likesUsersList.add(userId);
            }

            genreId = rs.getInt("genre_id");
            if (!rs.wasNull()) {
                String genreName = rs.getString("genre_name");
                genres.add(new Genre(genreId, genreName));
            }

            directorId = rs.getInt("director_id");
            if (!rs.wasNull()) {
                String directorName = rs.getString("director_name");
                directors.add(new Director(directorId, directorName));
            }
        }

        return film;
    }

}
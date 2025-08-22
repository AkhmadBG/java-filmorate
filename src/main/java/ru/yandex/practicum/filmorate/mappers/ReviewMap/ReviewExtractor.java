package ru.yandex.practicum.filmorate.mappers.ReviewMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ReviewExtractor implements ResultSetExtractor<Review> {

    @Override
    public Review extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.next()) {
            return null;
        }

        Review review = Review.builder()
                .reviewId(rs.getLong("review_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .userId(rs.getLong("user_id"))
                .filmId(rs.getLong("film_id"))
                .useful(rs.getInt("useful"))
                .userReactions(new HashMap<>())
                .build();

        // Обрабатываем первую реакцию
        long reactionUserId = rs.getLong("reaction_user_id");
        if (!rs.wasNull()) {
            Boolean isLike = rs.getBoolean("is_like");
            review.getUserReactions().put(reactionUserId, isLike);
        }

        // Обрабатываем остальные реакции
        while (rs.next()) {
            reactionUserId = rs.getLong("reaction_user_id");
            if (!rs.wasNull()) {
                Boolean isLike = rs.getBoolean("is_like");
                review.getUserReactions().put(reactionUserId, isLike);
            }
        }

        return review;
    }
}

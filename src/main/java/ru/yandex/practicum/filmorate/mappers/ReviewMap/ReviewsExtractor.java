package ru.yandex.practicum.filmorate.mappers.ReviewMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewsExtractor implements ResultSetExtractor<List<Review>> {
    @Override
    public List<Review> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Review> reviewMap = new HashMap<>();

        while (rs.next()) {

            Long reviewId = rs.getLong("review_id");

            Review review = reviewMap.get(reviewId);
            if (review == null) {
                review = Review.builder()
                        .reviewId(reviewId)
                        .content(rs.getString("content"))
                        .isPositive(rs.getBoolean("is_positive"))
                        .userId(rs.getLong("user_id"))
                        .filmId(rs.getLong("film_id"))
                        .useful(rs.getInt("useful"))
                        .userReactions(new HashMap<>())
                        .build();
                reviewMap.put(reviewId, review);
            }

            Long reactionUserId = rs.getLong("reaction_user_id");
            if (!rs.wasNull()) {
                Boolean isLike = rs.getBoolean("is_like");
                review.getUserReactions().put(reactionUserId, isLike);
            }
        }

        return new ArrayList<>(reviewMap.values());
    }
}


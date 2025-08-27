package ru.yandex.practicum.filmorate.mappers.reviewMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ReviewsExtractor implements ResultSetExtractor<List<Review>> {

    @Override
    public List<Review> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Review> reviewMap = new HashMap<>();

        while (rs.next()) {
            Integer reviewId = rs.getInt("review_id");

            Review review = reviewMap.get(reviewId);
            if (review == null) {
                review = Review.builder()
                        .reviewId(reviewId)
                        .content(rs.getString("content"))
                        .isPositive(rs.getBoolean("is_positive"))
                        .userId(rs.getInt("user_id"))
                        .filmId(rs.getInt("film_id"))
                        .useful(rs.getInt("useful"))
                        .userReactions(new LinkedHashMap<>())
                        .build();
                reviewMap.put(reviewId, review);
            }

            addReaction(rs, review);
        }

        List<Review> reviews = new ArrayList<>(reviewMap.values());
        reviews.sort((r1, r2) -> r2.getUseful().compareTo(r1.getUseful()));
        return reviews;
    }

    private void addReaction(ResultSet rs, Review review) throws SQLException {
        Long reactionUserId = rs.getLong("reaction_user_id");
        if (!rs.wasNull()) {
            Boolean isLike = rs.getBoolean("is_like");
            review.getUserReactions().put(reactionUserId, isLike);
        }
    }

}
package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.reviewMap.ReviewExtractor;
import ru.yandex.practicum.filmorate.mappers.reviewMap.ReviewsExtractor;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
@Slf4j
public class JdbcReviewRepository implements ReviewRepository {

    private final NamedParameterJdbcTemplate namedJdbc;

    @Override
    public Review createReview(Review review) {
        String sql = "INSERT INTO reviews (content, is_positive, user_id, film_id, useful)" +
                "VALUES (:content,:is_positive, :user_id, :film_id, :useful)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("content", review.getContent());
        params.addValue("is_positive", review.getIsPositive());
        params.addValue("user_id", review.getUserId());
        params.addValue("film_id", review.getFilmId());
        params.addValue("useful", review.getUseful() != null ? review.getUseful() : 0);


        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedJdbc.update(sql, params, keyHolder, new String[]{"review_id"});
        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("Создан отзыв с ID: {}", review.getReviewId());
        return review;

    }

    @Override
    public void updateReview(Review review) {
        String sql = "UPDATE reviews SET content = :content, is_positive = :is_positive, useful = :useful " +
                "WHERE review_id = :review_id";

        Map<String, Object> params = new HashMap<>();
        params.put("content", review.getContent());
        params.put("is_positive", review.getIsPositive());
        params.put("useful", review.getUseful());
        params.put("review_id", review.getReviewId());
        int updated = namedJdbc.update(sql, params);
        log.info("Обновлен {} отзыв с ID: {}", updated, review.getReviewId());


    }

    @Override
    public void deleteReview(int reviewId) {
        Review review = getReviewById(reviewId);
        String sql = "DELETE FROM reviews WHERE review_id = :review_id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("review_id", reviewId);

        int deleted = namedJdbc.update(sql, params);
        log.info("Удален {} отзыв с ID: {}", deleted, reviewId);
    }

    @Override
    public List<Review> getReviewsByFilmId(int filmId, int count) {
        if (count <= 0) {
            count = 10;
        }

        String sql;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("count", count);

        if (filmId == 0) {
            sql = """      
                    SELECT r.review_id, r.content, r.is_positive, r.user_id, r.film_id, r.useful,
                           rr.user_id AS reaction_user_id, rr.is_like
                    FROM (
                        SELECT *
                        FROM reviews
                        ORDER BY useful DESC, review_id ASC
                        LIMIT :count
                    ) r
                    LEFT JOIN review_reactions rr ON r.review_id = rr.review_id
                    """;
        } else {
            sql = """
                    SELECT r.review_id, r.content, r.is_positive, r.user_id, r.film_id, r.useful,
                           rr.user_id AS reaction_user_id, rr.is_like
                    FROM (
                        SELECT *
                        FROM reviews
                        WHERE film_id = :film_id
                        ORDER BY useful DESC, review_id ASC
                        LIMIT :count
                    ) r
                    LEFT JOIN review_reactions rr ON r.review_id = rr.review_id
                    """;

            params.addValue("film_id", filmId);
        }

        return namedJdbc.query(sql, params, new ReviewsExtractor());
    }

    @Override
    public Review getReviewById(int reviewId) {
        String sql = """
                SELECT r.review_id, r.content, r.is_positive, r.user_id, r.film_id, r.useful,
                       rr.user_id AS reaction_user_id, rr.is_like
                FROM reviews r
                LEFT JOIN review_reactions rr ON r.review_id = rr.review_id
                WHERE r.review_id = :review_id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("review_id", reviewId);

        Review review = namedJdbc.query(sql, params, new ReviewExtractor());

        if (review == null) {
            throw new NotFoundException("Отзыв с id " + reviewId + " не найден");
        }

        return review;
    }

    @Override
    public List<Review> getAllReviews(int count) {
        return getReviewsByFilmId(0, count);
    }

    @Override
    public void addLikeReview(int reviewId, int userId) {
        addReaction(reviewId, userId, true);
    }

    @Override
    public void addDislikeReview(int reviewId, int userId) {
        addReaction(reviewId, userId, false);
    }

    @Override
    public void deleteLikeReview(int reviewId, int userId) {
        deleteReaction(reviewId, userId);
    }

    @Override
    public void deleteDislikeReview(int reviewId, int userId) {
        deleteReaction(reviewId, userId);
    }

    private void addReaction(int reviewId, int userId, boolean isLike) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("review_id", reviewId)
                .addValue("user_id", userId)
                .addValue("is_like", isLike);

        try {
            String checkSql = "SELECT COUNT(*) FROM review_reactions WHERE review_id = :review_id AND user_id = :user_id";
            Integer count = namedJdbc.queryForObject(checkSql, params, Integer.class);

            if (count != null && count > 0) {
                String updateSql = "UPDATE review_reactions SET is_like = :is_like WHERE review_id = :review_id AND user_id = :user_id";
                namedJdbc.update(updateSql, params);
            } else {
                String insertSql = "INSERT INTO review_reactions (review_id, user_id, is_like) VALUES (:review_id, :user_id, :is_like)";
                namedJdbc.update(insertSql, params);
            }

            updateUseful(reviewId);

        } catch (Exception e) {
            log.error("Ошибка при добавлении реакции на отзыв {} пользователем {}: {}",
                    reviewId, userId, e.getMessage(), e);
            throw new RuntimeException("Ошибка при добавлении реакции на отзыв", e);
        }
    }

    private void deleteReaction(int reviewId, int userId) {
        String sql = "DELETE FROM review_reactions WHERE review_id = :review_id AND user_id = :user_id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("review_id", reviewId)
                .addValue("user_id", userId);

        try {
            namedJdbc.update(sql, params);
            updateUseful(reviewId);


            getReviewById(reviewId);

        } catch (Exception e) {
            log.error("Ошибка при удалении реакции на отзыв {} пользователем {}: {}",
                    reviewId, userId, e.getMessage(), e);
            throw new RuntimeException("Ошибка при удалении реакции на отзыв", e);
        }
    }

    private void updateUseful(int reviewId) {
        String sql = """
                    UPDATE reviews
                    SET useful = (
                        SELECT COALESCE(SUM(CASE WHEN is_like THEN 1 ELSE -1 END), 0)
                        FROM review_reactions
                        WHERE review_id = :review_id
                    )
                    WHERE review_id = :review_id
                """;

        namedJdbc.update(sql, new MapSqlParameterSource().addValue("review_id", reviewId));
    }

}
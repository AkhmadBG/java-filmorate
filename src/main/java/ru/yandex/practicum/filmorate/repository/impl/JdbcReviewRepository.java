package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.ReviewMap.ReviewExtractor;
import ru.yandex.practicum.filmorate.mappers.ReviewMap.ReviewsExtractor;
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
                    FROM reviews r
                    LEFT JOIN review_reactions rr ON r.review_id = rr.review_id
                    ORDER BY r.useful DESC
                    LIMIT :count
                    """;
        } else {
            sql = """
                    SELECT r.review_id, r.content, r.is_positive, r.user_id, r.film_id, r.useful,
                           rr.user_id AS reaction_user_id, rr.is_like
                    FROM reviews r
                    LEFT JOIN review_reactions rr ON r.review_id = rr.review_id
                    WHERE r.film_id = :film_id
                    ORDER BY r.useful DESC
                    LIMIT :count
                    """;
            params.addValue("film_id", filmId);
        }

        return namedJdbc.query(sql, params, new ReviewsExtractor());
    }

    @Override
    public void addLikeReview(int reviewId, int userId) {
        String selectSql = "SELECT is_like FROM review_reactions WHERE review_id = :review_id AND user_id = :user_id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("review_id", reviewId)
                .addValue("user_id", userId);

        Boolean oldReaction = null;
        try {
            oldReaction = namedJdbc.queryForObject(selectSql, params, Boolean.class);
        } catch (Exception ignored) {
            // записи нет
        }

        if (oldReaction == null) {
            // новая реакция
            namedJdbc.update("INSERT INTO review_reactions (review_id, user_id, is_like) VALUES (:review_id, :user_id, true)", params);
            namedJdbc.update("UPDATE reviews SET useful = useful + 1 WHERE review_id = :review_id", params);
        } else if (!oldReaction) {
            // было дизлайк → стало лайк
            namedJdbc.update("UPDATE review_reactions SET is_like = true WHERE review_id = :review_id AND user_id = :user_id", params);
            // откатить дизлайк (-1) и добавить лайк (+1) → +2
            namedJdbc.update("UPDATE reviews SET useful = useful + 2 WHERE review_id = :review_id", params);
        }
    }

    @Override
    public void addDislikeReview(int reviewId, int userId) {
        String selectSql = "SELECT is_like FROM review_reactions WHERE review_id = :review_id AND user_id = :user_id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("review_id", reviewId)
                .addValue("user_id", userId);

        Boolean oldReaction = null;
        try {
            oldReaction = namedJdbc.queryForObject(selectSql, params, Boolean.class);
        } catch (Exception ignored) {
            // записи нет
        }

        if (oldReaction == null) {
            // новая реакция
            namedJdbc.update("INSERT INTO review_reactions (review_id, user_id, is_like) VALUES (:review_id, :user_id, false)", params);
            namedJdbc.update("UPDATE reviews SET useful = useful - 1 WHERE review_id = :review_id", params);
        } else if (oldReaction) {
            // было лайк → стало дизлайк
            namedJdbc.update("UPDATE review_reactions SET is_like = false WHERE review_id = :review_id AND user_id = :user_id", params);
            // откатить лайк (+1) и добавить дизлайк (-1) → -2
            namedJdbc.update("UPDATE reviews SET useful = useful - 2 WHERE review_id = :review_id", params);
        }

    }

    @Override
    public void deleteLikeReview(int reviewId, int userId) {
        String selectSql = "SELECT is_like FROM review_reactions WHERE review_id = :review_id AND user_id = :user_id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("review_id", reviewId)
                .addValue("user_id", userId);

        Boolean oldReaction = null;
        try {
            oldReaction = namedJdbc.queryForObject(selectSql, params, Boolean.class);
        } catch (Exception ignored) {
            // записи нет
        }

        if (Boolean.TRUE.equals(oldReaction)) {
            // удаляем лайк
            namedJdbc.update("DELETE FROM review_reactions WHERE review_id = :review_id AND user_id = :user_id", params);
            namedJdbc.update("UPDATE reviews SET useful = useful - 1 WHERE review_id = :review_id", params);
        }
    }

    @Override
    public void deleteDislikeReview(int reviewId, int userId) {
        String selectSql = "SELECT is_like FROM review_reactions WHERE review_id = :review_id AND user_id = :user_id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("review_id", reviewId)
                .addValue("user_id", userId);

        Boolean oldReaction = null;
        try {
            oldReaction = namedJdbc.queryForObject(selectSql, params, Boolean.class);
        } catch (Exception ignored) {
            // записи нет
        }

        if (Boolean.FALSE.equals(oldReaction)) {
            // удаляем дизлайк
            namedJdbc.update("DELETE FROM review_reactions WHERE review_id = :review_id AND user_id = :user_id", params);
            namedJdbc.update("UPDATE reviews SET useful = useful + 1 WHERE review_id = :review_id", params);
        }

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
}

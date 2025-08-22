package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public void addLikeReview(int reviewId, int userId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("review_id", reviewId)
                .addValue("user_id", userId);

        String insertOrUpdateSql = """
                    INSERT INTO review_reactions (review_id, user_id, is_like)
                    VALUES (:review_id, :user_id, true)
                    ON CONFLICT (review_id, user_id) DO UPDATE
                    SET is_like = true
                    WHERE review_reactions.is_like = false
                    RETURNING CASE
                        WHEN xmax = 0 THEN 1       -- новая запись
                        ELSE 2                     -- обновление дизлайка → лайк
                    END AS delta
                """;

        Integer delta;
        try {
            delta = namedJdbc.queryForObject(insertOrUpdateSql, params, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            delta = null; // ничего не изменилось
        }

        if (delta != null) {
            namedJdbc.update(
                    "UPDATE reviews SET useful = useful + :delta WHERE review_id = :review_id",
                    new MapSqlParameterSource()
                            .addValue("delta", delta)
                            .addValue("review_id", reviewId)
            );
        }
    }

    @Override
    @Transactional
    public void addDislikeReview(int reviewId, int userId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("review_id", reviewId)
                .addValue("user_id", userId);

        String insertOrUpdateSql = """
                    INSERT INTO review_reactions (review_id, user_id, is_like)
                    VALUES (:review_id, :user_id, false)
                    ON CONFLICT (review_id, user_id) DO UPDATE
                    SET is_like = false
                    WHERE review_reactions.is_like = true
                    RETURNING CASE
                        WHEN xmax = 0 THEN -1      -- новая запись
                        ELSE -2                    -- лайк → дизлайк
                    END AS delta
                """;

        Integer delta;
        try {
            delta = namedJdbc.queryForObject(insertOrUpdateSql, params, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            delta = null;
        }

        if (delta != null) {
            namedJdbc.update(
                    "UPDATE reviews SET useful = useful + :delta WHERE review_id = :review_id",
                    new MapSqlParameterSource()
                            .addValue("delta", delta)
                            .addValue("review_id", reviewId)
            );
        }
    }

    @Override
    @Transactional
    public void deleteLikeReview(int reviewId, int userId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("review_id", reviewId)
                .addValue("user_id", userId);

        String deleteSql = """
                    DELETE FROM review_reactions
                    WHERE review_id = :review_id AND user_id = :user_id AND is_like = true
                    RETURNING review_id
                """;

        try {
            Integer affected = namedJdbc.queryForObject(deleteSql, params, Integer.class);
            if (affected != null) {
                namedJdbc.update(
                        "UPDATE reviews SET useful = GREATEST(useful - 1, 0) WHERE review_id = :review_id",
                        params
                );
            }
        } catch (EmptyResultDataAccessException ignored) {
        }
    }

    @Override
    @Transactional
    public void deleteDislikeReview(int reviewId, int userId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("review_id", reviewId)
                .addValue("user_id", userId);

        String deleteSql = """
                    DELETE FROM review_reactions
                    WHERE review_id = :review_id AND user_id = :user_id AND is_like = false
                    RETURNING review_id
                """;

        try {
            Integer affected = namedJdbc.queryForObject(deleteSql, params, Integer.class);
            if (affected != null) {
                namedJdbc.update(
                        "UPDATE reviews SET useful = useful + 1 WHERE review_id = :review_id",
                        params
                );
            }
        } catch (EmptyResultDataAccessException ignored) {
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

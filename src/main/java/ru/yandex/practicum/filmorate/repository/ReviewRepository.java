package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewRepository {

    Review createReview(Review review);

    void updateReview(Review review);

    void deleteReview(long reviewId);

    List<Review> getReviewsByFilmId(long filmId, int count);

    void addLikeReview(long reviewId, long userId);

    void addDislikeReview(long reviewId, long userId);

    void deleteLikeReview(long reviewId, long userId);

    void deleteDislikeReview(long reviewId, long userId);

    Review getReviewById(long reviewId);

}

package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewRepository {

    Review createReview(Review review);

    void updateReview(Review review);

    void deleteReview(int reviewId);

    List<Review> getReviewsByFilmId(int filmId, int count);

    void addLikeReview(int reviewId, int userId);

    void addDislikeReview(int reviewId, int userId);

    void deleteLikeReview(int reviewId, int userId);

    void deleteDislikeReview(int reviewId, int userId);

    Review getReviewById(int reviewId);

    List<Review> getAllReviews(int count);

}

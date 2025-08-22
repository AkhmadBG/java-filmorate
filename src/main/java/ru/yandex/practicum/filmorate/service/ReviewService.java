package ru.yandex.practicum.filmorate.service;


import ru.yandex.practicum.filmorate.repository.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.repository.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.repository.dto.review.UpdateReviewRequest;

import java.util.List;

public interface ReviewService {

    ReviewDto createReview(NewReviewRequest request);

    ReviewDto updateReview(UpdateReviewRequest request);

    void deleteReview(long reviewId);

    List<ReviewDto> getReviewsByFilmId(int filmId, int count);

    void addLikeReview(long reviewId, int userId);

    void addDislikeReview(long reviewId, int userId);

    void deleteLikeReview(long reviewId, int userId);

    void deleteDislikeReview(long reviewId, int userId);

    ReviewDto getReviewById(long reviewID);


}

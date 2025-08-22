package ru.yandex.practicum.filmorate.service;


import ru.yandex.practicum.filmorate.repository.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.repository.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.repository.dto.review.UpdateReviewRequest;

import java.util.List;

public interface ReviewService {

    ReviewDto createReview(NewReviewRequest request);

    ReviewDto updateReview(UpdateReviewRequest request);

    void deleteReview(long reviewId);

    List<ReviewDto> getReviewsByFilmId(long filmId, int count);

    void addLikeReview(long reviewId, long userId);

    void addDislikeReview(long reviewId, long userId);

    void deleteLikeReview(long reviewId, long userId);

    void deleteDislikeReview(long reviewId, long userId);

    ReviewDto getReviewById(long reviewID);


}

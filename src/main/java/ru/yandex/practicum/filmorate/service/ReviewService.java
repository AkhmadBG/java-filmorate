package ru.yandex.practicum.filmorate.service;


import ru.yandex.practicum.filmorate.repository.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.repository.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.repository.dto.review.UpdateReviewRequest;

import java.util.List;

public interface ReviewService {

    ReviewDto createReview(NewReviewRequest request);

    ReviewDto updateReview(UpdateReviewRequest request);

    void deleteReview(int reviewId);

    List<ReviewDto> getReviewsByFilmId(int filmId, int count);


    ReviewDto addLikeReview(int reviewId, int userId);

    ReviewDto addDislikeReview(int reviewId, int userId);

    ReviewDto deleteLikeReview(int reviewId, int userId);

    ReviewDto deleteDislikeReview(int reviewId, int userId);

    ReviewDto getReviewById(int reviewID);


}

package ru.yandex.practicum.filmorate.mappers.reviewMap;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.repository.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.repository.dto.review.UpdateReviewRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReviewMapper {

    public static Review mapToReview(NewReviewRequest rs) {
        Review review = new Review();
        review.setContent(rs.getContent());
        review.setIsPositive(rs.getIsPositive());
        review.setUserId(rs.getUserId());
        review.setFilmId(rs.getFilmId());
        return review;
    }

    public static ReviewDto mapToReviewDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewId(review.getReviewId());
        reviewDto.setFilmId(review.getFilmId());
        reviewDto.setUserId(review.getUserId());
        reviewDto.setContent(review.getContent());
        reviewDto.setIsPositive(review.getIsPositive());
        reviewDto.setUseful(review.getUseful());
        reviewDto.setUserReactions(review.getUserReactions());
        return reviewDto;

    }

    public static void updateReview(Review review, UpdateReviewRequest request) {
        if (request.getContent() != null) {
            review.setContent(request.getContent());

        }
        if (request.getIsPositive() != null) {
            review.setIsPositive(request.getIsPositive());
        }

    }

}

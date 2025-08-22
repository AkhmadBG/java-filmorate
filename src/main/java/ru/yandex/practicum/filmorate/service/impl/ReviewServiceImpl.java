package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mappers.ReviewMap.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.repository.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.repository.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.repository.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.util.ReviewValidator;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public ReviewDto createReview(NewReviewRequest request) {
        ReviewValidator.validator(request);
        Review review = reviewRepository.createReview(ReviewMapper.mapToReview(request));
        log.info("ReviewServiceImpl: новый отзыв  с id {} добавлен", review.getReviewId());
        return ReviewMapper.mapToReviewDto(review);
    }

    @Override
    public ReviewDto updateReview(UpdateReviewRequest request) {
        Review review = reviewRepository.getReviewById(request.getReviewId());
        ReviewMapper.updateReview(review, request);
        reviewRepository.updateReview(review);
        log.info("ReviewServiceImpl: отзыв  с id {} обновлен", review.getReviewId());
        return ReviewMapper.mapToReviewDto(review);
    }

    @Override
    public void deleteReview(long reviewId) {
        reviewRepository.deleteReview((int) reviewId);
        log.info("ReviewServiceImpl: отзыв с id {} удален", reviewId);
    }

    @Override
    public List<ReviewDto> getReviewsByFilmId(long filmId, int count) {
        return reviewRepository.getReviewsByFilmId((int) filmId, count)
                .stream()
                .map(ReviewMapper::mapToReviewDto)
                .toList();
    }

    @Override
    public void addLikeReview(long reviewId, long userId) {
        reviewRepository.addLikeReview((int) reviewId, (int) userId);
        log.info("ReviewServiceImpl: пользователь {} лайкнул отзыв {}", userId, reviewId);
    }

    @Override
    public void addDislikeReview(long reviewId, long userId) {
        reviewRepository.addDislikeReview((int) reviewId, (int) userId);
        log.info("ReviewServiceImpl: пользователь {} дизлайкнул отзыв {}", userId, reviewId);
    }

    @Override
    public void deleteLikeReview(long reviewId, long userId) {
        reviewRepository.deleteLikeReview((int) reviewId, (int) userId);
        log.info("ReviewServiceImpl: пользователь {} убрал лайк с отзыва {}", userId, reviewId);
    }

    @Override
    public void deleteDislikeReview(long reviewId, long userId) {
        reviewRepository.deleteDislikeReview((int) reviewId, (int) userId);
        log.info("ReviewServiceImpl: пользователь {} убрал дизлайк с отзыва {}", userId, reviewId);
    }

    @Override
    public ReviewDto getReviewById(long reviewId) {
        Review review = reviewRepository.getReviewById(reviewId);
        return ReviewMapper.mapToReviewDto(review);
    }
}

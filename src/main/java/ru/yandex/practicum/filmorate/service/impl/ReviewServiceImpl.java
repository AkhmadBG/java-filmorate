package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.ReviewMap.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
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
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewDto createReview(NewReviewRequest request) {

        ReviewValidator.validator(request);

        if (userRepository.getUserById(request.getUserId()) == null) {
            throw new NotFoundException("Пользователь с id " + request.getUserId() + " не найден");
        }

        if (filmRepository.getFilmById(request.getFilmId()) == null) {
            throw new NotFoundException("Фильм с id " + request.getFilmId() + " не найден");
        }
        Review review = reviewRepository.createReview(ReviewMapper.mapToReview(request));
        log.info("ReviewServiceImpl: новый отзыв  с id {} добавлен", review.getReviewId());
        return ReviewMapper.mapToReviewDto(review);
    }

    @Override
    public ReviewDto updateReview(UpdateReviewRequest request) {
        Review review = reviewRepository.getReviewById(request.getReviewId());
        if (review == null) {
            throw new NotFoundException("Отзыв не найден");
        }

        ReviewMapper.updateReview(review, request);
        reviewRepository.updateReview(review);
        log.info("ReviewServiceImpl: отзыв с id {} обновлен", review.getReviewId());
        return ReviewMapper.mapToReviewDto(review);
    }

    @Override
    public void deleteReview(int reviewId) {
        Review review = reviewRepository.getReviewById(reviewId);
        if (review == null) {
            throw new NotFoundException("Отзыв не найден");
        }
        reviewRepository.deleteReview(reviewId);
        log.info("ReviewServiceImpl: отзыв с id {} удален", reviewId);
    }

    @Override
    public List<ReviewDto> getReviewsByFilmId(int filmId, int count) {

        if (count <= 0) {
            count = 10;
        }

        if (filmId > 0 && filmRepository.getFilmById(filmId) == null) {
            throw new NotFoundException("Фильма с id " + filmId + " не существует");
        }

        List<Review> reviews;
        if (filmId == 0) {
            reviews = reviewRepository.getAllReviews(count);
        } else {
            reviews = reviewRepository.getReviewsByFilmId(filmId, count);
        }

        return reviews.stream()
                .map(ReviewMapper::mapToReviewDto)
                .toList();
    }

    @Override
    public void addLikeReview(int reviewId, int userId) {
        Review review = reviewRepository.getReviewById(reviewId);
        if (review == null) throw new NotFoundException("Отзыв не найден");
        if (userRepository.getUserById(userId) == null) throw new NotFoundException("Пользователь не найден");
        reviewRepository.addLikeReview(reviewId, userId);
        log.info("ReviewServiceImpl: пользователь {} лайкнул отзыв {}", userId, reviewId);
    }

    @Override
    public void addDislikeReview(int reviewId, int userId) {
        Review review = reviewRepository.getReviewById(reviewId);
        if (review == null) throw new NotFoundException("Отзыв не найден");
        if (userRepository.getUserById(userId) == null) throw new NotFoundException("Пользователь не найден");
        reviewRepository.addDislikeReview(reviewId, userId);
        log.info("ReviewServiceImpl: пользователь {} дизлайкнул отзыв {}", userId, reviewId);
    }

    @Override
    public void deleteLikeReview(int reviewId, int userId) {
        Review review = reviewRepository.getReviewById(reviewId);
        if (review == null) throw new NotFoundException("Отзыв не найден");
        if (userRepository.getUserById(userId) == null) throw new NotFoundException("Пользователь не найден");
        reviewRepository.deleteLikeReview(reviewId, userId);
        log.info("ReviewServiceImpl: пользователь {} убрал лайк с отзыва {}", userId, reviewId);
    }

    @Override
    public void deleteDislikeReview(int reviewId, int userId) {
        Review review = reviewRepository.getReviewById(reviewId);
        if (review == null) throw new NotFoundException("Отзыв не найден");
        if (userRepository.getUserById(userId) == null) throw new NotFoundException("Пользователь не найден");
        reviewRepository.deleteDislikeReview(reviewId, userId);
        log.info("ReviewServiceImpl: пользователь {} убрал дизлайк с отзыва {}", userId, reviewId);
    }

    @Override
    public ReviewDto getReviewById(int reviewId) {
        Review review = reviewRepository.getReviewById(reviewId);
        if (review == null) throw new NotFoundException("Отзыв не найден");
        return ReviewMapper.mapToReviewDto(review);
    }
}

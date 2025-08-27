package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.repository.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.repository.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.repository.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody NewReviewRequest request) {
        ReviewDto reviewDto = reviewService.createReview(request);
        log.info("Получен запрос на добавление отзыва");
        return ResponseEntity.ok(reviewDto);
    }

    @PutMapping
    public ResponseEntity<ReviewDto> updateReview(@RequestBody UpdateReviewRequest request) {
        ReviewDto reviewDto = reviewService.updateReview(request);
        log.info("Получен запрос на обновление отзыва {}", request.getReviewId());
        return ResponseEntity.ok(reviewDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable int id) {
        log.info("Получен запрос на удаление отзыва {}", id);
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable int id) {
        log.info("Получен запрос на получение отзыва {}", id);
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviewsByFilm(@RequestParam(defaultValue = "0") int filmId,
                                                            @RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос на получение отзывов по фильму {} (count = {})", filmId, count);
        return ResponseEntity.ok(reviewService.getReviewsByFilmId(filmId, count));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<ReviewDto> addLike(@PathVariable int id, @PathVariable int userId) {
        reviewService.addLikeReview(id, userId);
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @PutMapping("/{id}/dislike/{userId}")
    public ResponseEntity<ReviewDto> addDislike(@PathVariable int id, @PathVariable int userId) {
        reviewService.addDislikeReview(id, userId);
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<ReviewDto> deleteLike(@PathVariable int id, @PathVariable int userId) {
        reviewService.deleteLikeReview(id, userId);
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public ResponseEntity<ReviewDto> deleteDislike(@PathVariable int id, @PathVariable int userId) {
        reviewService.deleteDislikeReview(id, userId);
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

}
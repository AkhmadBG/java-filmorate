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

    // POST /reviews
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody NewReviewRequest request) {
        ReviewDto reviewDto = reviewService.createReview(request);
        log.info("Получен запрос на добавление отзыва");
        return ResponseEntity.ok(reviewDto);
    }

    // PUT /reviews
    @PutMapping
    public ResponseEntity<ReviewDto> updateReview(@RequestBody UpdateReviewRequest request) {
        ReviewDto reviewDto = reviewService.updateReview(request);
        log.info("Получен запрос на обновление отзыва {}", request.getReviewId());
        return ResponseEntity.ok(reviewDto);
    }

    // DELETE /reviews/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable long id) {
        log.info("Получен запрос на удаление отзыва {}", id);
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    // GET /reviews/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable long id) {
        log.info("Получен запрос на получение отзыва {}", id);
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    // GET /reviews?filmId={filmId}&count={count}
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviewsByFilm(
            @RequestParam(defaultValue = "0") int filmId,
            @RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос на получение отзывов по фильму {} (count = {})", filmId, count);
        return ResponseEntity.ok(reviewService.getReviewsByFilmId(filmId, count));
    }

    // PUT /reviews/{id}/like/{userId}
    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable long id, @PathVariable int userId) {
        log.info("Пользователь {} лайкает отзыв {}", userId, id);
        reviewService.addLikeReview(id, userId);
        return ResponseEntity.noContent().build();
    }

    // PUT /reviews/{id}/dislike/{userId}
    @PutMapping("/{id}/dislike/{userId}")
    public ResponseEntity<Void> addDislike(@PathVariable long id, @PathVariable int userId) {
        log.info("Пользователь {} дизлайкает отзыв {}", userId, id);
        reviewService.addDislikeReview(id, userId);
        return ResponseEntity.noContent().build();
    }

    // DELETE /reviews/{id}/like/{userId}
    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> deleteLike(@PathVariable long id, @PathVariable int userId) {
        log.info("Пользователь {} удаляет лайк отзыва {}", userId, id);
        reviewService.deleteLikeReview(id, userId);
        return ResponseEntity.noContent().build();
    }

    // DELETE /reviews/{id}/dislike/{userId}
    @DeleteMapping("/{id}/dislike/{userId}")
    public ResponseEntity<Void> deleteDislike(@PathVariable long id, @PathVariable int userId) {
        log.info("Пользователь {} удаляет дизлайк отзыва {}", userId, id);
        reviewService.deleteDislikeReview(id, userId);
        return ResponseEntity.noContent().build();
    }
}

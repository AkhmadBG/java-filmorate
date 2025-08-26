package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.repository.dto.review.NewReviewRequest;
import ru.yandex.practicum.filmorate.repository.dto.review.ReviewDto;
import ru.yandex.practicum.filmorate.repository.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.service.ReviewService;


import java.net.URI;


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
        log.info("Создан новый отзыв id={}", reviewDto.getReviewId());
        return ResponseEntity
                .created(URI.create("/reviews/" + reviewDto.getReviewId()))
                .body(reviewDto); // HTTP 201 Created
    }

    // PUT /reviews
    @PutMapping
    public ResponseEntity<ReviewDto> updateReview(@RequestBody UpdateReviewRequest request) {
        ReviewDto reviewDto = reviewService.updateReview(request);
        log.info("Отзыв id={} обновлен", request.getReviewId());
        return ResponseEntity.ok(reviewDto);
    }

    //DELETE /reviews/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable int id) {
        reviewService.deleteReview(id);
        log.info("Отзыв id={} удален", id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

    // GET /reviews/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable int id) {
        ReviewDto review = reviewService.getReviewById(id);
        log.info("Получен отзыв id={}", id);
        return ResponseEntity.ok(review);
    }

    // GET /reviews?filmId={filmId}&count={count}
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviewsByFilm(
            @RequestParam(required = false) Integer filmId,
            @RequestParam(defaultValue = "10") int count) {
        List<ReviewDto> reviews = reviewService.getReviewsByFilmId(filmId == null ? 0 : filmId, count);
        log.info("Получено {} отзывов (filmId={}, count={})", reviews.size(), filmId, count);
        return ResponseEntity.ok(reviews);


    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<ReviewDto> addLike(@PathVariable int id, @PathVariable int userId) {

        ReviewDto updated = reviewService.addLikeReview(id, userId);
        return ResponseEntity.ok(updated);

    }

    @PutMapping("/{id}/dislike/{userId}")
    public ResponseEntity<ReviewDto> addDislike(@PathVariable int id, @PathVariable int userId) {
        ReviewDto updated = reviewService.addDislikeReview(id, userId);
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<ReviewDto> deleteLike(@PathVariable int id, @PathVariable int userId) {
        ReviewDto updated = reviewService.deleteLikeReview(id, userId);
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public ResponseEntity<ReviewDto> deleteDislike(@PathVariable int id, @PathVariable int userId) {
        ReviewDto updated = reviewService.deleteDislikeReview(id, userId);
        return ResponseEntity.ok(updated);
    }


}


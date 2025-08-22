package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ReviewDto createReview(@RequestBody NewReviewRequest request) {
        log.info("Получен запрос на добавление отзыва");
        return reviewService.createReview(request);
    }

    // PUT /reviews
    @PutMapping
    public ReviewDto updateReview(@RequestBody UpdateReviewRequest request) {
        log.info("Получен запрос на обновление отзыва {}", request.getReviewId());
        return reviewService.updateReview(request);
    }

    // DELETE /reviews/{id}
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable long id) {
        log.info("Получен запрос на удаление отзыва {}", id);
        reviewService.deleteReview(id);
    }

    // GET /reviews/{id}
    @GetMapping("/{id}")
    public ReviewDto getReviewById(@PathVariable long id) {
        log.info("Получен запрос на получение отзыва {}", id);
        return reviewService.getReviewById(id);
    }

    // GET /reviews?filmId={filmId}&count={count}
    @GetMapping
    public List<ReviewDto> getReviewsByFilm(
            @RequestParam(defaultValue = "0") long filmId,
            @RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос на получение отзывов по фильму {} (count = {})", filmId, count);
        return reviewService.getReviewsByFilmId(filmId, count);
    }

    // PUT /reviews/{id}/like/{userId}
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь {} лайкает отзыв {}", userId, id);
        reviewService.addLikeReview(id, userId);
    }

    // PUT /reviews/{id}/dislike/{userId}
    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь {} дизлайкает отзыв {}", userId, id);
        reviewService.addDislikeReview(id, userId);
    }

    // DELETE /reviews/{id}/like/{userId}
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь {} удаляет лайк отзыва {}", userId, id);
        reviewService.deleteLikeReview(id, userId);
    }

    // DELETE /reviews/{id}/dislike/{userId}
    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь {} удаляет дизлайк отзыва {}", userId, id);
        reviewService.deleteDislikeReview(id, userId);
    }
}

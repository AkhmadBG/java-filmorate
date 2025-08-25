package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.filmMap.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.service.RecommendationService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final UserRepository userRepository;
    private final FilmRepository filmRepository;

    @Override
    public List<FilmDto> getRecommendations(int userId) {
        log.info("Формирование рекомендаций для пользователя с ID: {}", userId);

        if (!userRepository.userExists(userId)) {
            throw new NotFoundException("Пользователь с ID: " + userId + " не найден");
        }

        log.info("Поиск пользователей с общими лайками");
        Map<Integer, Integer> similarUsers = userRepository.findUsersWithCommonLikes(userId);

        if (similarUsers.isEmpty()) {
            log.info("Не найдено пользователей с общими лайками для пользователя ID: {}", userId);
            return Collections.emptyList();
        }

        log.info("Выбор самого похожего пользователя (с максимальным количеством общих лайков");
        Integer mostSimilarUserId = findMostSimilarUser(similarUsers);
        log.info("Найден похожий пользователь ID: {} (общих лайков: {})",
                mostSimilarUserId, similarUsers.get(mostSimilarUserId));

        log.info("Получение рекомендованных фильмов");
        List<Film> recommendedFilms = filmRepository.getRecommendedFilms(mostSimilarUserId, userId);

        log.info("Для пользователя ID: {} рекомендовано {} фильмов",
                userId, recommendedFilms.size());

        log.info("Преобразование в DTO");
        return recommendedFilms.stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    private Integer findMostSimilarUser(Map<Integer, Integer> similarUsers) {
        return similarUsers.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalStateException("Не удалось найти похожего пользователя"));
    }

}
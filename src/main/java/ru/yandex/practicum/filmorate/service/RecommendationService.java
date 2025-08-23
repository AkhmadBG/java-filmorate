package ru.yandex.practicum.filmorate.service;


import ru.yandex.practicum.filmorate.repository.dto.FilmDto;

import java.util.List;

/**
 * Сервис для предоставления рекомендаций по фильмам
 */
public interface RecommendationService {

    /**
     * Получает персональные рекомендации фильмов для пользователя
     * на основе алгоритма коллаборативной фильтрации
     */
    List<FilmDto> getRecommendations(int userId);
}
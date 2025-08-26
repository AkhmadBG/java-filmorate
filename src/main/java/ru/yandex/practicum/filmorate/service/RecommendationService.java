package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.repository.dto.film.FilmDto;

import java.util.List;

public interface RecommendationService {

    List<FilmDto> getRecommendations(int userId);

}
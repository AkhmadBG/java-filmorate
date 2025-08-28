package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.UserEvents;
import ru.yandex.practicum.filmorate.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class FeedEventService {

    private final UserRepository userRepository;

    public void addEvent(int userId, int entityId, UserEvents.EventType eventType, UserEvents.Operation operation) {
        userRepository.addFeedEvent(userId, entityId, eventType, operation);
    }
}

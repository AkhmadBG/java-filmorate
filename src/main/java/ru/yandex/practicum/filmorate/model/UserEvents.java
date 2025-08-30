package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEvents {
    private long timestamp;
    private int userId;
    private EventType eventType;
    private Operation operation;
    private int eventId;
    private int entityId;

    public enum EventType {
        LIKE, REVIEW, FRIEND
    }

    public enum Operation {
        ADD, REMOVE, UPDATE
    }
}
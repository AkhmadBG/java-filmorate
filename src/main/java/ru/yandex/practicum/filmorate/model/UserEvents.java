package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEvents {

    private Long timestamp;

    private int userId;

    private String eventType;

    private String operation;

    private int eventId;

    private int entityId;

}
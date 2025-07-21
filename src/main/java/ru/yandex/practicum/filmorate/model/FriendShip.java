package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class FriendShip {

    private Integer userId;

    private Integer friendId;

    private boolean status;

}

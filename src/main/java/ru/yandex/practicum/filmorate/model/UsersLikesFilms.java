package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersLikesFilms {

    private User user;

    @Builder.Default
    private Set<Integer> usersLikesFilmsIds = new HashSet<>();

}


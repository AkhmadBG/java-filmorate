package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Mpa {

    private final Integer id;

    private final String name;

}

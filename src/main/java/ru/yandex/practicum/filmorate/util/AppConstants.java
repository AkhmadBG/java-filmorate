package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class AppConstants {

    public static final LocalDate STARTING_DATE = LocalDate.of(1895, 12, 28);

    public static final int MAX_DESCRIPTION_LENGTH = 200;

}
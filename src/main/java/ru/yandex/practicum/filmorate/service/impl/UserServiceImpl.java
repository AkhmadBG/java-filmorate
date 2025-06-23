package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.AppValidation;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User addUser(User user) {
        log.debug("начало валидации параметров пользователя при добавлении нового");
        AppValidation.userValidation(user);
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        if (!userStorage.getAllUsers().contains(user)) {
            log.error("пользователя не существует");
            throw new NotFoundException("пользователя не существует");
        }
        log.debug("начало валидации параметров пользователя при обновлении существующего");
        AppValidation.userValidation(user);
        return userStorage.updateUser(user);
    }

}

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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User getUserById(int userId) {
        return userStorage.getUserById(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User addUser(User user) {
        log.debug("Начало валидации параметров пользователя при добавлении нового");
        AppValidation.userValidation(user);
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        if (!userStorage.getAllUsers().contains(user)) {
            log.error("Пользователя не существует");
            throw new NotFoundException("пользователя не существует");
        }
        log.debug("Начало валидации параметров пользователя при обновлении существующего");
        AppValidation.userValidation(user);
        return userStorage.updateUser(user);
    }

    @Override
    public User addFriend(int userId, int friendId) {
        log.debug("Пользователи с id {} и {} теперь друзья", userId, friendId);
        return userStorage.addFriend(userId, friendId);
    }

    @Override
    public User deleteFriend(int userId, int friendId) {
        log.debug("Пользователи с id {} и {} теперь не друзья", userId, friendId);
        return userStorage.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> userFriends(int userId) {
        log.debug("Запрос списка друзей пользователя с id {}", userId);
        return userStorage.userFriends(userId);
    }

    @Override
    public Collection<User> commonFriends(int userId, int otherId) {
        log.debug("Запрос списка общих друзей пользователей с id {} и {}", userId, otherId);
        return userStorage.commonFriends(userId, otherId);
    }

}

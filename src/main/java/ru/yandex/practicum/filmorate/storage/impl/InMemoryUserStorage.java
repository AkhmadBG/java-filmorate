package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int identifier = 0;

    @Override
    public User getUserById(int userId) {
        if (!users.containsKey(userId)) {
            log.error("Пользователь с id {} не найден в InMemoryUserStorage", userId);
            throw new NotFoundException("Пользователь с id " + userId + "не найден");
        }
        return users.get(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Получение списка пользователей из InMemoryUserStorage");
        return users.values();
    }

    @Override
    public User addUser(User user) {
        user.setId(++identifier);
        users.put(user.getId(), user);
        log.info("Новый пользователь с id {} добавлен в InMemoryUserStorage", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        log.info("Пользователь с id {} обновлен в InMemoryUserStorage", user.getId());
        return user;
    }

    @Override
    public User addFriend(int userId, int friendId) {
        if (!users.containsKey(userId)) {
            log.error("Пользователь с id {} не найден в InMemoryUserStorage", userId);
            throw new NotFoundException("Пользователь с id " + userId + "не найден в InMemoryUserStorage");
        }
        if (!users.containsKey(friendId)) {
            log.error("Пользователь с id {} не найден в InMemoryUserStorage", friendId);
            throw new NotFoundException("Пользователь с id " + friendId + "не найден в InMemoryUserStorage");
        }
        if (userId == friendId) {
            log.error("Попытка добавления пользователя с id {} к себе в друзья пользователя с id {} в InMemoryUserStorage",
                    userId, friendId);
            throw new ValidationException("Попытка добавления пользователя к себе в друзья");
        }
        User user = users.get(userId);
        User userFriend = users.get(friendId);
        user.getFriendsId().add(friendId);
        userFriend.getFriendsId().add(userId);
        log.info("Пользователь с id {} добавлен в друзья пользователю с id {} в InMemoryUserStorage", userId, friendId);
        return users.get(userId);
    }

    @Override
    public User deleteFriend(int userId, int friendId) {
        if (!users.containsKey(userId)) {
            log.error("Пользователь с id {} не найден в InMemoryUserStorage", userId);
            throw new NotFoundException("Пользователь с id " + userId + "не найден в InMemoryUserStorage");
        }
        if (!users.containsKey(friendId)) {
            log.error("Пользователь с id {} не найден в InMemoryUserStorage", friendId);
            throw new NotFoundException("Пользователь с id " + friendId + "не найден в InMemoryUserStorage");
        }
        if (userId == friendId) {
            log.error("Попытка удаления пользователя с id {} из друзей пользователя с id {} в InMemoryUserStorage",
                    userId, friendId);
            throw new ValidationException("Попытка добавления пользователя к себе в друзья в InMemoryUserStorage");
        }
        User user = users.get(userId);
        User userFriend = users.get(friendId);
        user.getFriendsId().remove(friendId);
        userFriend.getFriendsId().remove(userId);
        log.info("Пользователь с id {} удален из друзей пользователя с id {} в InMemoryUserStorage", userId, friendId);
        return users.get(userId);
    }

    @Override
    public List<User> userFriends(int userId) {
        User user = users.get(userId);
        if (user == null) {
            log.error("Пользователь с id {} не найден в InMemoryUserStorage", userId);
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        Set<Integer> friendsId = user.getFriendsId();
        if (friendsId == null || friendsId.isEmpty()) {
            return Collections.emptyList();
        }
        log.info("Запрошен список друзей пользователя с id {} в InMemoryUserStorage", userId);
        return friendsId.stream()
                .map(users::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> commonFriends(int userId, int otherId) {
        if (!users.containsKey(userId)) {
            log.error("Пользователь с id {} не найден в InMemoryUserStorage", userId);
            throw new NotFoundException("Пользователь с id " + userId + "не найден");
        }
        if (!users.containsKey(otherId)) {
            log.error("Пользователь с id {} не найден в InMemoryUserStorage", otherId);
            throw new NotFoundException("Пользователь с id " + otherId + "не найден");
        }
        Set<Integer> userFriends = Optional.ofNullable(users.get(userId).getFriendsId())
                .orElse(Collections.emptySet());
        Set<Integer> otherUserFriends = Optional.ofNullable(users.get(otherId).getFriendsId())
                .orElse(Collections.emptySet());
        Set<Integer> commonFriendsIds = userFriends.stream()
                .filter(otherUserFriends::contains)
                .collect(Collectors.toSet());
        log.info("Запрошен список общих друзей пользователей с id {} и с id {} в InMemoryUserStorage", userId, otherId);
        return commonFriendsIds.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

}

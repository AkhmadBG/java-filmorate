package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserStorageImpl implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int identifier = 0;

    @Override
    public Collection<User> getAllUsers() {
        log.info("получение списка пользователей из UserStorageImpl");
        return users.values();
    }

    @Override
    public User addUser(User user) {
        user.setId(++identifier);
        users.put(user.getId(), user);
        log.info("новый пользователь с id {} добавлен в UserStorageImpl", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        log.info("пользователь с id {} обновлен в UserStorageImpl", user.getId());
        return user;
    }

}

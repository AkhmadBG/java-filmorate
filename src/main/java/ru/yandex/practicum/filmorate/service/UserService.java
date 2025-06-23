package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {

    Collection<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

}

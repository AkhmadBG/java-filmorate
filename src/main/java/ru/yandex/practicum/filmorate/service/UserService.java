package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    User getUserById(int userId);

    Collection<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

    User addFriend(int userId, int friendId);

    User deleteFriend(int userId, int friendId);

    List<User> userFriends(int userId);

    Collection<User> commonFriends(int userId, int otherId);

}

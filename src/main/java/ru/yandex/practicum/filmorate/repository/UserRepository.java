package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {

    boolean userExists(int userId);

    User getUserById(int userId);

    List<User> allUsers();

    User addUser(User user);

    void addFriendShips(int userId, int friendId);

    void deleteFriendShip(int userId, int friendId);

    void updateUser(User user);

    void deleteUser(int userId);
}
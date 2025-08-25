package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserRepository {

    boolean userExists(int userId);

    User getUserById(int userId);

    List<User> allUsers();

    User addUser(User user);

    void addFriendShips(int userId, int friendId);

    void deleteFriendShip(int userId, int friendId);

    void updateUser(User user);

    void deleteUser(int userId);
    /**
     * Находит пользователей с общими лайками с указанным пользователем
     * @param userId ID пользователя для поиска похожих
     * @return Map, где ключ - ID похожего пользователя, значение - количество общих лайков
     */
    Map<Integer, Integer> findUsersWithCommonLikes(int userId);
}
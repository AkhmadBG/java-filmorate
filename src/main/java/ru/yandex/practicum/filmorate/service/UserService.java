package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.repository.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.repository.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.repository.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(NewUserRequest newUserRequest);

    UserDto updateUser(UpdateUserRequest updateUserRequestUser);

    List<UserDto> allUsers();

    UserDto getUserById(int userId);

    void addFriends(int userId, int friendId);

    void removeFriends(int userId, int friendId);

    List<UserDto> getFriendsList(int userId);

    List<UserDto> getCommonFriendsList(int userId, int otherId);

    void deleteUser(int userId);
}
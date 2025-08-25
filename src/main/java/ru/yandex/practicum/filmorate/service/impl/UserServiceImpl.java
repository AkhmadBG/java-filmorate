package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.userMap.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserEvents;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.repository.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.repository.dto.user.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.util.UserValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {
        UserValidator.validator(newUserRequest);
        User user = userRepository.addUser(UserMapper.mapToUser(newUserRequest));
        log.info("UserServiceImpl: добавлен пользователь с id {} ", user.getId());
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUser(UpdateUserRequest updateUserRequest) {
        User user = userRepository.getUserById(updateUserRequest.getId());
        if (user == null) {
            throw new NotFoundException("пользователь с id " + updateUserRequest.getId() + " не найден");
        }
        UserMapper.updateUser(user, updateUserRequest);
        userRepository.updateUser(user);
        log.info("UserServiceImpl: данные пользователя с id {} обновлены ", user.getId());
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> allUsers() {
        List<User> allUsers = userRepository.allUsers();
        log.info("UserServiceImpl: запрошен список всех пользователей, всего пользователей {}", allUsers.size());
        return allUsers.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(int userId) {
        log.info("UserServiceImpl: запрошен пользователь с id {} ", userId);
        User user = userRepository.getUserById(userId);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public void addFriends(int userId, int friendId) {
        if (userId == friendId) {
            throw new ValidationException("UserServiceImpl: попытка добавления пользователя к себе в друзья");
        }
        userRepository.addFriendShips(userId, friendId);
        log.info("UserServiceImpl: пользователи с id " + userId + " и " + friendId + "теперь друзья");
    }

    @Override
    public void removeFriends(int userId, int friendId) {
        if (userId == friendId) {
            throw new ValidationException("UserServiceImpl: попытка добавления пользователя к себе в друзья");
        }
        userRepository.deleteFriendShip(userId, friendId);
        log.info("UserServiceImpl: пользователи с id " + userId + " и " + friendId + "больше не друзья");
    }

    @Override
    public List<UserDto> getFriendsList(int userId) {
        User user = userRepository.getUserById(userId);
        List<Integer> friendsId = new ArrayList<>(user.getFriends());
        log.info("UserServiceImpl: запрошен список друзей пользователя " + userId + " всего их " + friendsId.size());
        return friendsId.stream()
                .map(userRepository::getUserById)
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getCommonFriendsList(int userId, int otherId) {
        User user = userRepository.getUserById(userId);
        User otherUser = userRepository.getUserById(otherId);
        Set<Integer> friendsList = new HashSet<>(user.getFriends());
        friendsList.retainAll(otherUser.getFriends());
        List<Integer> commonFriendsList = new ArrayList<>(friendsList);
        log.info("UserServiceImpl: запрошен список общих друзей пользователей с id " + userId + " и " + otherId);
        return commonFriendsList.stream()
                .map(userRepository::getUserById)
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(int userId) {
        if (!userRepository.userExists(userId)) {
            throw new NotFoundException("UserServiceImpl: пользователь с id: " + userId + " не найден");
        }
        userRepository.deleteUser(userId);
        log.info("UserServiceImpl: пользователь с id: {} удалён", userId);
    }

    @Override
    public List<UserEvents> getUserFeeds(int userId) {
        return userRepository.userEvent(userId);
    }

}
package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.UserEvents;
import ru.yandex.practicum.filmorate.repository.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.repository.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.repository.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.repository.dto.user.UserDto;
import ru.yandex.practicum.filmorate.service.RecommendationService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody NewUserRequest newUserRequest) {
        UserDto userDto = userService.addUser(newUserRequest);
        log.info("UserController: добавлен новый пользователь: {}", userDto.getId());
        return ResponseEntity.ok(userDto);
    }

    @PutMapping()
    public ResponseEntity<UserDto> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        UserDto userDto = userService.updateUser(updateUserRequest);
        log.info("UserController: данные пользователя обновлены: {}", userDto.getId());
        return ResponseEntity.ok(userDto);
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> allUsers() {
        List<UserDto> allUsers = userService.allUsers();
        log.info("UserController: количество всех пользователей: {}", allUsers.size());
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable(value = "userId") int userId) {
        log.info("UserController: запрошен пользователь с id: {}", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> addFriends(@PathVariable(value = "userId") int userId,
                                           @PathVariable(value = "friendId") int friendId) {
        userService.addFriends(userId, friendId);
        log.info("UserController: пользователи с id {} и id {} добавлены в друзья", userId, friendId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable(value = "userId") int userId,
                                             @PathVariable(value = "friendId") int friendId) {
        userService.removeFriends(userId, friendId);
        log.info("UserController: пользователи с id {} и id {} удалены из друзей", userId, friendId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<UserDto>> getFriendsList(@PathVariable(value = "userId") int userId) {
        log.info("UserController: запрошен пользователь с id: {}", userId);
        return ResponseEntity.ok(userService.getFriendsList(userId));
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public ResponseEntity<List<UserDto>> getCommonFriendsList(@PathVariable(value = "userId") int userId,
                                                              @PathVariable(value = "otherId") int otherId) {
        log.info("UserController: запрошен список общих друзей пользователей с id {} и id {}", userId, otherId);
        return ResponseEntity.ok(userService.getCommonFriendsList(userId, otherId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        log.info("UserController: пользователь с id: {} удалён", userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/recommendations")
    public ResponseEntity<List<FilmDto>> getRecommendations(@PathVariable int userId) {
        log.info("Запрос рекомендаций для пользователя с ID: {}", userId);
        List<FilmDto> recommendations = recommendationService.getRecommendations(userId);
        log.info("Возвращено {} рекомендаций для пользователя ID: {}", recommendations.size(), userId);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/{id}/feed")
    public ResponseEntity<List<UserEvents>> getUserFeeds(@PathVariable(value = "id") int userId) {
        log.info("UserController: запрошена лента событий для пользователя id {}", userId);
        return ResponseEntity.ok(userService.getUserFeeds(userId));
    }

}
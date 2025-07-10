package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Запрошен список всех пользователей");
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("Запрос на добавление нового пользователя");
        userService.addUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        userService.updateUser(user);
        log.info("Запрос на обновление пользователя с id {}", user.getId());
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id,
                          @PathVariable int friendId) {
        log.info("Запрос от пользователя с id {} на добавление в друзья пользователю с id {}", friendId, id);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable int id,
                             @PathVariable int friendId) {
        log.info("Запрос от пользователя с id {} на исключение из друзей пользователя с id {}", friendId, id);
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> userFriends(@PathVariable int id) {
        log.info("Запрос списка друзей пользователя с id {}", id);
        return userService.userFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> commonFriends(@PathVariable int id,
                                          @PathVariable int otherId) {
        log.info("Запрос списка общих друзей пользователей с id {} и {}", id, otherId);
        return userService.commonFriends(id, otherId);
    }

}

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
        log.info("запрошен список всех пользователей");
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        userService.addUser(user);
        log.info("добавлен новый пользователь с id {}", user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        userService.updateUser(user);
        log.info("пользователь с id {} обновлен", user.getId());
        return user;
    }

}

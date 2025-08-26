package ru.yandex.practicum.filmorate.mappers.userMap;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.repository.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.repository.dto.user.UserDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

    public static User mapToUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setEmail(newUserRequest.getEmail());
        user.setLogin(newUserRequest.getLogin());
        user.setName(newUserRequest.getName());
        user.setBirthday(newUserRequest.getBirthday());
        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setName(user.getName());
        userDto.setBirthday(user.getBirthday());
        userDto.setFriends(user.getFriends());
        return userDto;
    }

    public static void updateUser(User user, UpdateUserRequest updateUserRequest) {
        if (updateUserRequest.hasEmail()) {
            user.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.hasLogin()) {
            user.setLogin(updateUserRequest.getLogin());
        }
        if (updateUserRequest.hasName()) {
            user.setName(updateUserRequest.getName());
        }
        if (updateUserRequest.hasBirthday()) {
            user.setBirthday(updateUserRequest.getBirthday());
        }
    }

}
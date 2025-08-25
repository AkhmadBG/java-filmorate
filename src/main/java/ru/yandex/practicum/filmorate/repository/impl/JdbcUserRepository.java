package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserExtractor;
import ru.yandex.practicum.filmorate.mappers.UsersExtractor;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final NamedParameterJdbcOperations namedJdbc;

    @Override
    public boolean userExists(int userId) {
        String query = "SELECT COUNT(*) FROM users WHERE user_id = :user_id";
        Map<String, Object> params = Map.of("user_id", userId);
        Integer count = namedJdbc.queryForObject(query, params, Integer.class);
        log.info("JdbcUserRepository: проверка существования пользователя с id: {}", userId);
        return count == null || count > 0;
    }

    @Override
    public User getUserById(int userId) {
        String queryUser = "SELECT u.user_id, u.name, u.email, u.login, u.birthday, uf.friend_id " +
                "FROM users u " +
                "LEFT JOIN user_friendships uf ON u.user_id = uf.user_id " +
                "WHERE u.user_id = :user_id ";
        if (!userExists(userId)) {
            throw new NotFoundException("пользователь с id: " + userId + " не найден");
        }
        try {
            log.info("JdbcUserRepository: запрос пользователя с id: {}", userId);
            Map<String, Object> params = Map.of("user_id", userId);
            return namedJdbc.query(queryUser, params, new UserExtractor());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("UserRepository: не удалось получить пользователя с id: " + userId);
        }
    }

    @Override
    public void addFriendShips(int userId, int friendId) {
        String queryFriend = "INSERT INTO user_friendships (user_id, friend_id) VALUES (:user_id, :friend_id)";
        if (!userExists(userId)) {
            throw new NotFoundException("UserRepository: пользователь с id: " + userId + " не найден");
        }
        if (!userExists(friendId)) {
            throw new NotFoundException("UserRepository: пользователь с id: " + friendId + " не найден");
        }
        try {
            Map<String, Object> params = Map.of("user_id", userId, "friend_id", friendId);
            namedJdbc.update(queryFriend, params);
            log.info("UserRepository: пользователи с id: {} и {} теперь друзья", userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("UserRepository: попытка добавления в друзья пользователю с id: " + userId +
                    " пользователя с id: " + friendId + " не удалась");
        }
    }

    @Override
    public void deleteFriendShip(int userId, int friendId) {
        String queryDeleteFriend = "DELETE FROM user_friendships WHERE user_id = :user_id AND friend_id = :friend_id";
        if (!userExists(userId)) {
            throw new NotFoundException("UserRepository: пользователь с id: " + userId + " не найден");
        }
        if (!userExists(friendId)) {
            throw new NotFoundException("UserRepository: пользователь с id: " + friendId + " не найден");
        }
        Map<String, Object> params = Map.of("user_id", userId, "friend_id", friendId);
        namedJdbc.update(queryDeleteFriend, params);
        log.info("UserRepository: пользователи с id: {} и {} теперь не друзья", userId, friendId);
    }

    @Override
    public List<User> allUsers() {
        String queryUsers = "SELECT u.*, uf.friend_id " +
                "FROM users u " +
                "LEFT JOIN user_friendships uf ON u.user_id = uf.user_id";
        List<User> allUsers = namedJdbc.query(queryUsers, new UsersExtractor());
        log.info("UserRepository: количество зарегистрированных пользователей: {}", allUsers.size());
        return allUsers;
    }

    @Override
    public void updateUser(User user) {
        if (!userExists(user.getId())) {
            throw new NotFoundException("UserRepository: пользователь с id: " + user.getId() + " не найден");
        }
        try {
            String queryUpdateUser = "UPDATE users SET email = :email, login = :login,  name = :name, " +
                    "birthday = :birthday WHERE user_id = :user_id";

            Map<String, Object> params = Map.of("user_id", user.getId(),
                    "email", user.getEmail(),
                    "login", user.getLogin(),
                    "name", user.getName(),
                    "birthday", user.getBirthday());

            namedJdbc.update(queryUpdateUser, params);
            log.info("UserRepository: пользователь с id: {} обновлен", user.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("UserRepository: попытка обновления пользователя с id: " + user.getId() + " не удалась");
        }
    }

    @Override
    public User addUser(User user) {
        String query = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (:email, :login, :name, :birthday)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        namedJdbc.update(query, params, keyHolder, new String[]{"user_id"});

        Integer id = keyHolder.getKeyAs(Integer.class);
        if (id != null) {
            user.setId(id);
            log.info("UserRepository: добавлен новый пользователь с id: {}", user.getId());
            return user;
        } else {
            throw new RuntimeException("UserRepository: не удалось сохранить пользователя: id не сгенерирован");
        }
    }

}
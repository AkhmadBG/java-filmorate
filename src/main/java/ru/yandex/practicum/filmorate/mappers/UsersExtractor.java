package ru.yandex.practicum.filmorate.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UsersExtractor implements ResultSetExtractor<List<User>> {

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, User> userMap = new HashMap<>();

        while (rs.next()) {
            int userId = rs.getInt("user_id");

            User user = userMap.get(userId);
            if (user == null) {
                user = User.builder()
                        .id(userId)
                        .name(rs.getString("name"))
                        .email(rs.getString("email"))
                        .login(rs.getString("login"))
                        .birthday(rs.getDate("birthday") != null ? rs.getDate("birthday").toLocalDate() : null)
                        .friends(new HashSet<>())
                        .build();
                userMap.put(userId, user);
            }

            Integer friendId = rs.getInt("friend_id");
            if (!rs.wasNull()) {
                user.getFriends().add(friendId);
            }
        }

        return new ArrayList<>(userMap.values());
    }

}
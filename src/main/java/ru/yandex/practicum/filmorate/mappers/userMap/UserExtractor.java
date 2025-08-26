package ru.yandex.practicum.filmorate.mappers.userMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class UserExtractor implements ResultSetExtractor<User> {

    @Override
    public User extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.next()) {
            return null;
        }

        User user = User.builder()
                .id(rs.getInt("user_id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(new HashSet<>())
                .build();

        Set<Integer> friends = user.getFriends();

        Integer friendId = rs.getInt("friend_id");
        if (!rs.wasNull()) {
            friends.add(friendId);
        }

        while (rs.next()) {
            friendId = rs.getInt("friend_id");
            if (!rs.wasNull()) {
                friends.add(friendId);
            }
        }

        return user;
    }

}
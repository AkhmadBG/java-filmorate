package ru.yandex.practicum.filmorate.mappers.userMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.UserEvents;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserEventsExtractor implements ResultSetExtractor<List<UserEvents>> {

    @Override
    public List<UserEvents> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<UserEvents> events = new ArrayList<>();
        while (rs.next()) {
            UserEvents userEvents = UserEvents.builder()
                    .timestamp(rs.getLong("timestamp"))
                    .userId(rs.getInt("user_id"))
                    .eventType(rs.getString("event_type"))
                    .operation(rs.getString("operation"))
                    .eventId(rs.getInt("event_id"))
                    .entityId(rs.getInt("entity_id"))
                    .build();
            events.add(userEvents);
        }
        return events;
    }

}

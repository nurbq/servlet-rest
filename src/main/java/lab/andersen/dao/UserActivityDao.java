package lab.andersen.dao;

import lab.andersen.entity.UserActivity;
import lab.andersen.entity.UserActivityShort;
import lab.andersen.exception.DaoException;
import lab.andersen.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserActivityDao {
    private static final String FIND_ALL = "SELECT id, user_id, description, date_time FROM users_activities;";
    private static final String FIND_ALL_TODAY_ACTIVITIES = "SELECT u.name, ua.description, ua.date_time " +
                                                            "FROM users_activities ua " +
                                                            "         left join users u on ua.user_id = u.id " +
                                                            "WHERE DATE(date_time) = CURRENT_DATE";

    public List<UserActivity> findAll() {
        List<UserActivity> activities = new ArrayList<>();
        try (
                Connection connection = ConnectionManager.open();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)
        ) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                activities.add(new UserActivity(
                        rs.getObject("id", Integer.class),
                        rs.getObject("user_id", Integer.class),
                        rs.getObject("description", String.class),
                        rs.getObject("date_time", Timestamp.class))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return activities;
    }

    public List<UserActivityShort> findAllToday() throws DaoException {
        List<UserActivityShort> activities = new ArrayList<>();
        try (
                Connection connection = ConnectionManager.open();
                PreparedStatement statement = connection.prepareStatement(FIND_ALL_TODAY_ACTIVITIES)
        ) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                UserActivityShort userActivity = new UserActivityShort(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp("date_time").toLocalDateTime()
                );
                activities.add(userActivity);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return activities;
    }
}

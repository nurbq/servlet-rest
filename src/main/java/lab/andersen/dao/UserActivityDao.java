package lab.andersen.dao;

import lab.andersen.entity.UserActivity;
import lab.andersen.entity.UserActivityExtended;
import lab.andersen.entity.UserActivityShort;
import lab.andersen.exception.DaoException;
import lab.andersen.exception.UserActivityNotFoundException;
import lab.andersen.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserActivityDao {
    private static final String FIND_ALL = "SELECT id, user_id, description, date_time FROM users_activities;";
    private static final String FIND_ALL_WITH_USERNAMES =
            "SELECT a.id, a.user_id, a.description, a.date_time, u.name as username, u.surname as surname FROM users_activities as a left join users as u on u.id = a.user_id  order by a.id;";
    private static final String FIND_BY_ID = "SELECT id, user_id, description, date_time FROM users_activities WHERE id = ?";
    private static final String CREATE_USER_ACTIVITY = "INSERT INTO users_activities(user_id, description) VALUES (?, ?)";
    private static final String UPDATE_USER_ACTIVITY =
            "UPDATE users_activities SET user_id = ?, description = ?, date_time = ? WHERE id = ?;";
    private static final String DELETE_USER_ACTIVITY = "DELETE FROM users_activities WHERE id = ?";

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

    public List<UserActivityExtended> findAllAddUsername() throws DaoException {
        List<UserActivityExtended> activities = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_WITH_USERNAMES)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UserActivityExtended userActivity = new UserActivityExtended(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("date_time"),
                        resultSet.getString("username") + " " + resultSet.getString("surname")
                );
                activities.add(userActivity);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return activities;
    }

    public Optional<UserActivity> findById(int id) throws DaoException {
        UserActivity userActivity = null;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userActivity = new UserActivity(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("date_time")
                );
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return Optional.ofNullable(userActivity);
    }

    public void create(UserActivity entity) throws DaoException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(CREATE_USER_ACTIVITY)) {
            statement.setInt(1, entity.getUserId());
            statement.setString(2, entity.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void update(UserActivity entity) throws DaoException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_ACTIVITY)) {
            if (findById(entity.getId()).isPresent()) {
                statement.setInt(1, entity.getUserId());
                statement.setString(2, entity.getDescription());
                statement.setTimestamp(3, entity.getDateTime());
                statement.setInt(4, entity.getId());
                statement.executeUpdate();
            } else {
                throw new UserActivityNotFoundException("user activity with id=%d doesn't exist".formatted(entity.getId()));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void delete(int id) throws DaoException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_ACTIVITY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}

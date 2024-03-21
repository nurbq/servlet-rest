package lab.andersen.dao;

import lab.andersen.entity.User;
import lab.andersen.exception.DaoException;
import lab.andersen.exception.UserNotFoundException;
import lab.andersen.util.ConnectionManager;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {

    private static final String FIND_ALL_USERS = "SELECT id, age, surname, name, password FROM users order by id;";
    private static final String FIND_USER_BY_ID = "SELECT id, age, surname, name, password FROM users WHERE id = ?";
    private static final String CREATE_USER = "INSERT INTO users(age, surname, name, password) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE users SET age = ?, surname = ?, name = ?, password = ? WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";
    private static final String FIND_BY_NAME_AND_PASSWORD = "SELECT id, name, password, surname, age " +
                                                            "FROM users " +
                                                            "WHERE name = ? and password = ?";
    private static final String FIND_ID_BY_NAME = "SELECT id FROM users WHERE name = ?";

    public List<User> findAll() throws DaoException {
        List<User> allUsers = new ArrayList<>();
        try (
                Connection connection = ConnectionManager.open();
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL_USERS);
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getInt("age"),
                        resultSet.getString("surname"),
                        resultSet.getString("name"),
                        resultSet.getString("password")
                );
                allUsers.add(user);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return allUsers;
    }

    @SneakyThrows
    public Optional<User> findByNameAndPassword(String name, String password) {
        try (
                Connection connection = ConnectionManager.open();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME_AND_PASSWORD)
        ) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            User user = null;

            if (resultSet.next()) {
                user = new User(
                        resultSet.getObject("id", Integer.class),
                        resultSet.getObject("age", Integer.class),
                        resultSet.getObject("surname", String.class),
                        resultSet.getObject("name", String.class),
                        resultSet.getObject("password", String.class)
                );
            }
            return Optional.ofNullable(user);
        }
    }

    public Integer create(User user) {
        try (
                Connection connection = ConnectionManager.open();
                PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER)
        ) {
            preparedStatement.setInt(1, user.getAge());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getPassword());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Optional<User> findById(int id) throws DaoException {
        User user = null;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_ID)
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getInt("age"),
                        resultSet.getString("surname"),
                        resultSet.getString("name"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return Optional.ofNullable(user);
    }


    public int update(User entity) throws DaoException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER)
        ) {
            if (findById(entity.getId()).isPresent()) {
                statement.setInt(1, entity.getAge());
                statement.setString(2, entity.getSurname());
                statement.setString(3, entity.getName());
                statement.setString(4, entity.getPassword());
                statement.setInt(5, entity.getId());
                int i = statement.executeUpdate();
                return i;
            } else {
                throw new UserNotFoundException(String.format("User with id=%d doesn't exist", entity.getId()));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void delete(int id) throws DaoException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Integer findIdByName(String name) {
        Integer userId = null;
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(FIND_ID_BY_NAME)
        ) {
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userId;
    }
}

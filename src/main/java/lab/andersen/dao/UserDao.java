package lab.andersen.dao;

import lab.andersen.entity.User;
import lab.andersen.exception.DaoException;
import lab.andersen.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static final String FIND_ALL_USERS = "SELECT id, age, surname, name FROM users order by id;";
    private static final String CREATE_USER = "INSERT INTO users(age, surname, name) VALUES (?, ?, ?)";

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
                        resultSet.getString("name")
                );
                allUsers.add(user);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return allUsers;
    }

    public void create(User user) {
        try (
                Connection connection = ConnectionManager.open();
                PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER)
        ) {
            preparedStatement.setInt(1, user.getAge());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

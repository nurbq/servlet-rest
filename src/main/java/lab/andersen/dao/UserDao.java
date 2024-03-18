package lab.andersen.dao;

import lab.andersen.entity.User;
import lab.andersen.exception.DaoException;
import lab.andersen.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static final UserDao INSTANCE = new UserDao();

    private UserDao() {
    }

    private static final String FIND_ALL_USERS = "SELECT id, age, surname, name FROM users order by id;";


    public List<User> findAll() throws DaoException {
        List<User> allUsers = new ArrayList<>();
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
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


    public static UserDao getInstance() {
        return INSTANCE;
    }

}

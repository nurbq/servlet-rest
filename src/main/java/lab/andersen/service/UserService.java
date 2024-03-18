package lab.andersen.service;

import lab.andersen.dao.UserDao;
import lab.andersen.entity.User;
import lab.andersen.exception.DaoException;

import java.util.List;

public class UserService {

    private static final UserService INSTANCE = new UserService();
    private final UserDao userDao = UserDao.getInstance();

    private UserService() {
    }

    public List<User> findAll()  {
        try {
            return userDao.findAll();
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}

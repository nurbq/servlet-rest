package lab.andersen.service;

import lab.andersen.dao.UserDao;
import lab.andersen.entity.User;
import lab.andersen.exception.DaoException;

import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    public List<User> findAll()  {
        try {
            return userDao.findAll();
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }
}

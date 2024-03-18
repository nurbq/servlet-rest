package lab.andersen.service;

import lab.andersen.dao.UserDao;
import lab.andersen.entity.User;
import lab.andersen.exception.DaoException;
import lab.andersen.exception.ServiceException;
import lab.andersen.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

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

    public Integer create(User user) {
        return userDao.create(user);
    }

    public User findById(int id) throws ServiceException {
        try {
            Optional<User> optionalUser = userDao.findById(id);
            if (optionalUser.isPresent()) {
                return optionalUser.get();
            }
            throw new UserNotFoundException(String.format("User with id=%d doesn't exist", id));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void update(User user) throws ServiceException {
        try {
            userDao.update(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void delete(int id) throws ServiceException {
        try {
            userDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}

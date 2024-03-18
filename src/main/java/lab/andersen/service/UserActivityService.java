package lab.andersen.service;

import lab.andersen.dao.UserActivityDao;
import lab.andersen.entity.UserActivity;
import lab.andersen.entity.UserActivityShort;
import lab.andersen.exception.DaoException;
import lab.andersen.exception.ServiceException;
import lab.andersen.exception.UserActivityNotFoundException;

import java.util.List;
import java.util.Optional;

public class UserActivityService {

    private final UserActivityDao userActivityDao;

    public UserActivityService(UserActivityDao userActivityDao) {
        this.userActivityDao = userActivityDao;
    }

    public List<UserActivityShort> findAllTodayActivities() throws ServiceException {
        try {
            return userActivityDao.findAllToday();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<UserActivity> findAllUsersActivities() throws ServiceException {
        return userActivityDao.findAll();
    }

    public UserActivity findById(int id) throws ServiceException {
        try {
            Optional<UserActivity> optionalUserActivity = userActivityDao.findById(id);
            if (optionalUserActivity.isPresent()) {
                return optionalUserActivity.get();
            } else {
                throw new UserActivityNotFoundException("user activity with id=%d doesn't exist".formatted(id));
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void create(UserActivity userActivity) throws ServiceException {
        try {
            userActivityDao.create(userActivity);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void update(UserActivity userActivity) throws ServiceException {
        try {
            userActivityDao.update(userActivity);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void delete(int id) throws ServiceException {
        try {
            userActivityDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}

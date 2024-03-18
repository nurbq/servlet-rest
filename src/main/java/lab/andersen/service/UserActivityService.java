package lab.andersen.service;

import lab.andersen.dao.UserActivityDao;
import lab.andersen.entity.UserActivityShort;
import lab.andersen.exception.DaoException;
import lab.andersen.exception.ServiceException;

import java.util.List;

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
}

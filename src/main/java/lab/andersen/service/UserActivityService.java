package lab.andersen.service;

import lab.andersen.dao.UserActivityDao;
import lab.andersen.dto.CreateUserActivityDto;
import lab.andersen.dto.UserActivityDto;
import lab.andersen.dto.UserActivityShortDto;
import lab.andersen.entity.UserActivity;
import lab.andersen.exception.DaoException;
import lab.andersen.exception.ServiceException;
import lab.andersen.exception.UserActivityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserActivityService {

    private final UserActivityDao userActivityDao;

    public UserActivityService(UserActivityDao userActivityDao) {
        this.userActivityDao = userActivityDao;
    }

    public List<UserActivityShortDto> findAllTodayActivities() throws ServiceException {
        try {
            return userActivityDao.findAllToday();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<UserActivityDto> findAllUsersActivities() {
        return userActivityDao.findAll()
                .stream()
                .map(userActivity -> new UserActivityDto(
                        userActivity.getUserId(),
                        userActivity.getDescription(),
                        userActivity.getDateTime()))
                .collect(Collectors.toList());
    }

    public UserActivityDto findById(int id) throws ServiceException {
        try {
            Optional<UserActivity> optionalUserActivity = userActivityDao.findById(id);
            if (optionalUserActivity.isPresent()) {
                return optionalUserActivity.map(it -> new UserActivityDto(
                                it.getUserId(),
                                it.getDescription(),
                                it.getDateTime()))
                        .get();
            } else {
                throw new UserActivityNotFoundException("user activity with id=" + id + " doesn't exist");
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public int create(String name, CreateUserActivityDto userActivity) throws ServiceException {
        try {
            return userActivityDao.create(name, userActivity.getDescription());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public int update(UserActivity userActivity) throws ServiceException {
        try {
            return userActivityDao.update(userActivity);
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

    public List<UserActivityShortDto> findAllByName(String name) {
        return userActivityDao.findAllByName(name);
    }
}

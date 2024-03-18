package lav.andersen.service;

import lab.andersen.dao.UserDao;
import lab.andersen.entity.User;
import lab.andersen.exception.DaoException;
import lab.andersen.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class UserServiceUnitTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    public UserServiceUnitTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findAllUsers_returnsUsers() throws DaoException {
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new User(1, 20, "TestName1", "testname2", "password"));
        expectedUsers.add(new User(2, 21, "TestName2", "testname3", "password"));

        when(userDao.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.findAll();

        verify(userDao, times(1)).findAll();

        Assertions.assertEquals(expectedUsers.size(), actualUsers.size());
    }

}

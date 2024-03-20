package lab.andersen.controller;

import com.google.gson.Gson;
import lab.andersen.dao.UserDao;
import lab.andersen.dto.UserDto;
import lab.andersen.entity.User;
import lab.andersen.exception.UserNotFoundException;
import lab.andersen.service.UserService;
import lab.andersen.servlet.FrontController;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class UsersController extends FrontController {

    private final UserService userService = new UserService(new UserDao());
    private final Gson gson = new Gson();

    @SneakyThrows
    @Override
    public void process() throws ServletException, IOException {
        response.setContentType("application/json");
        String pathInfo = request.getPathInfo();
        String[] split = pathInfo.split("/");
        try (PrintWriter writer = response.getWriter()) {
//            writer.write(pathInfo);
//            writer.write(split.length);
            if (split.length <= 2) {
                List<UserDto> all = userService.findAll();
                writer.write(gson.toJson(all));
            } else {
                Integer userId = Integer.getInteger(split[2]);
                writer.write(userId);
//                if (userId != null) {
//                    User userById = userService.findById(userId);
//                    writer.write(gson.toJson(userById));
//                } else {
//                    throw new UserNotFoundException("User not found");
//                }
            }
        }
    }
}

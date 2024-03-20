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
        try (PrintWriter writer = response.getWriter()) {
            writer.print(request.getPathInfo());
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                List<UserDto> all = userService.findAll();
                writer.print(gson.toJson(all));
            } else {
                String[] split = pathInfo.split("/");
                System.out.println(Arrays.toString(split));
                Integer s = Integer.getInteger(split[2]);
                if (s != null) {
                    User userById = userService.findById(s);
                    writer.print(gson.toJson(userById));
                } else {
                    throw new UserNotFoundException("User not found");
                }
            }
        }
    }
}

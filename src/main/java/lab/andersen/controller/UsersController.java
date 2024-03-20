package lab.andersen.controller;

import com.google.gson.Gson;
import lab.andersen.dao.UserDao;
import lab.andersen.dto.UserDto;
import lab.andersen.entity.User;
import lab.andersen.exception.ServiceException;
import lab.andersen.service.UserService;
import lab.andersen.servlet.FrontController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UsersController extends FrontController {

    private final UserService userService = new UserService(new UserDao());
    private final Gson gson = new Gson();

    @Override
    public void process() throws ServletException, IOException {
        response.setContentType("application/json");
        String pathInfo = request.getPathInfo();
        String[] split = pathInfo.split("/");
        try (PrintWriter writer = response.getWriter()) {
            if (split.length <= 2) {
                List<UserDto> all = userService.findAll();
                writer.write(gson.toJson(all));
            } else {
                int userId = Integer.parseInt(split[2]);
                User userById = userService.findById(userId);
                writer.write(gson.toJson(userById));
            }
        } catch (RuntimeException | ServiceException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}

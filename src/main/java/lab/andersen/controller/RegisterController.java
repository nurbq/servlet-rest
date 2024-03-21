package lab.andersen.controller;

import com.google.gson.Gson;
import lab.andersen.dao.UserDao;
import lab.andersen.dto.enums.HttpMethods;
import lab.andersen.entity.User;
import lab.andersen.service.UserService;
import lab.andersen.servlet.FrontController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class RegisterController extends FrontController {
    private final UserService userService = new UserService(new UserDao());
    private final Gson gson = new Gson();
    @Override
    public void process() throws ServletException, IOException {
        response.setContentType("application/json");
        String method = request.getMethod();
        String pathInfo = request.getPathInfo();
        String[] splitPath = pathInfo.split("/");
        try (
                BufferedReader reader = request.getReader();
                PrintWriter writer = response.getWriter()
        ) {
            if (method.equalsIgnoreCase(HttpMethods.POST.name())) {
                if (splitPath.length <= 2) {
                    String userLines = reader.lines().collect(Collectors.joining());
                    User user = gson.fromJson(userLines, User.class);
                    Integer i = userService.create(user);
                    writer.print(i);
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        }
    }
}

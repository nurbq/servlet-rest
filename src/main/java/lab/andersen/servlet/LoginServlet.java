package lab.andersen.servlet;


import com.google.gson.Gson;
import lab.andersen.dao.UserDao;
import lab.andersen.dto.LoginUserDto;
import lab.andersen.dto.UserDto;
import lab.andersen.service.UserService;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService(new UserDao());
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (
                BufferedReader reader = req.getReader();
        ) {
            String collect = reader.lines().collect(Collectors.joining());
            LoginUserDto loginUserDto = gson.fromJson(collect, LoginUserDto.class);
            userService.login(loginUserDto.getName(), loginUserDto.getPassword())
                    .ifPresentOrElse(
                            user -> onLoginSuccess(user, req, resp),
                            () -> onLoginFail(req, resp)
                    );
        }
    }

    @SneakyThrows
    private void onLoginFail(HttpServletRequest req, HttpServletResponse resp) {
        resp.sendRedirect("/login?error");
    }

    @SneakyThrows
    private void onLoginSuccess(UserDto user, HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().setAttribute("user", user);
        resp.sendRedirect("/users");
    }
}

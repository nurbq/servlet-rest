package lab.andersen.servlet;

import com.google.gson.Gson;
import lab.andersen.entity.User;
import lab.andersen.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String pathInfo = req.getPathInfo();
        List<User> users = userService.findAll();
        try (PrintWriter writer = resp.getWriter()) {
            if (pathInfo == null || pathInfo.equals("/")) {
                writer.write(gson.toJson(users));
            }
        }
    }
}

package lab.andersen.servlet;

import com.google.gson.Gson;
import lab.andersen.dao.UserDao;
import lab.andersen.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user-activities/*")
public class UserActivityServlet extends HttpServlet {

    private final UserService userService = new UserService(new UserDao());

    private final Gson gson = new Gson();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}

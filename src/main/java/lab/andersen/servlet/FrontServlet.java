package lab.andersen.servlet;

import lab.andersen.controller.UnknownController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@WebServlet("/front/*")
public class FrontServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FrontController controller = getController(req);
        controller.init(getServletContext(), req, resp);
        controller.process();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FrontController controller = getController(req);
        controller.init(getServletContext(), req, resp);
        controller.process();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FrontController controller = getController(req);
        controller.init(getServletContext(), req, resp);
        controller.process();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FrontController controller = getController(req);
        controller.init(getServletContext(), req, resp);
        controller.process();
    }

    private FrontController getController(HttpServletRequest req) {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null) {
                return new UnknownController();
            }
            String[] split = pathInfo.split("/");
            if (split.length < 1) {
                return new UnknownController();
            }
            String className = split[1];
            Class<?> type = Class.forName(String.format("lab.andersen.controller." + "%sController", className));

            Object controllerObject = type.getDeclaredConstructor().newInstance();

            if (controllerObject instanceof FrontController) {
                return (FrontController) controllerObject;
            } else {
                throw new IllegalArgumentException("Invalid controller class: " + className);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

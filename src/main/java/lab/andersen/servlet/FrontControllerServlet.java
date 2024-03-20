package lab.andersen.servlet;

import lab.andersen.exception.UnknownCommand;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/front/*")
public class FrontControllerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FrontCommand command = getCommand(req);
        command.init(getServletContext(), req, resp);
        command.process();
    }

    private FrontCommand getCommand(HttpServletRequest req) {
        try {
            String commandClassName = req.getParameter("command");
            String pathInfo = req.getPathInfo();
            String[] split = pathInfo.split("/");
            if (split.length < 1) {
                return new UnknownCommand();
            }
            if (commandClassName == null || commandClassName.isEmpty()) {
                return new UnknownCommand();
            }
            String className = split[1];
            Class<?> type = Class.forName(String.format(
                    "lab.andersen.controller."
                    + "%sCommand", className));

            Object commandObject = type.getDeclaredConstructor().newInstance();

            if (commandObject instanceof FrontCommand) {
                return (FrontCommand) commandObject;
            } else {
                throw new IllegalArgumentException("Invalid command class: " + commandClassName);
            }
        } catch (Exception e) {
            return new UnknownCommand();
        }
    }
}

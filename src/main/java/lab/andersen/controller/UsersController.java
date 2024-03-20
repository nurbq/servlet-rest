package lab.andersen.controller;

import lab.andersen.servlet.FrontController;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

public class UsersController extends FrontController {
    @Override
    public void process() throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(request.getPathInfo());
        }
    }
}

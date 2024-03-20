package lab.andersen.controller;

import lab.andersen.servlet.FrontCommand;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class UserCommand extends FrontCommand {
    @Override
    public void process() throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json");

        try (PrintWriter writer = response.getWriter()) {
            writer.print(request.getPathInfo());
        }
    }
}

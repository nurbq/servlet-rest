package lab.andersen.controller;

import com.google.gson.Gson;
import lab.andersen.dao.UserDao;
import lab.andersen.dto.UserDto;
import lab.andersen.dto.enums.HttpMethods;
import lab.andersen.entity.User;
import lab.andersen.exception.ServiceException;
import lab.andersen.service.UserService;
import lab.andersen.servlet.FrontController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class UsersController extends FrontController {

    private final UserService userService = new UserService(new UserDao());
    private final Gson gson = new Gson();

    @Override
    public void process() throws IOException {
        response.setContentType("application/json");
        String method = request.getMethod();
        String pathInfo = request.getPathInfo();
        String[] splitPath = pathInfo.split("/");

        if (method.equalsIgnoreCase(HttpMethods.GET.name())) {
            handleGet(request, response, splitPath);
        } else if (method.equalsIgnoreCase(HttpMethods.POST.name())) {
            handlePost(request, response, splitPath);
        } else if (method.equalsIgnoreCase(HttpMethods.PUT.name())) {
            handlePut(request, response, splitPath);
        } else if (method.equalsIgnoreCase(HttpMethods.DELETE.name())) {
            handleDelete(request, response, splitPath);
        } else {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    private void handleGet(HttpServletRequest request, HttpServletResponse response, String[] splitPath) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            if (splitPath.length <= 2) {
                List<UserDto> all = userService.findAll();
                writer.write(gson.toJson(all));
            } else {
                int userId = Integer.parseInt(splitPath[2]);
                User userById = userService.findById(userId);
                writer.write(gson.toJson(userById));
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to parse user_id to number");
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response, String[] splitPath) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            if (splitPath.length <= 2) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                int id = Integer.parseInt(splitPath[2]);
                writer.print(id);
                userService.delete(id);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to parse userId to number");
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handlePost(HttpServletRequest request, HttpServletResponse response, String[] splitPath) throws IOException {
        try (
                BufferedReader reader = request.getReader();
                PrintWriter writer = response.getWriter()
        ) {
            String userLines = reader.lines().collect(Collectors.joining());
            User user = gson.fromJson(userLines, User.class);
            Integer userId = userService.create(user);

            writer.print(userId);
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    private void handlePut(HttpServletRequest request, HttpServletResponse response, String[] splitPath) throws IOException {
        try (
                BufferedReader reader = request.getReader();
                PrintWriter writer = response.getWriter()
        ) {
            String jsonUser = reader.lines().collect(Collectors.joining());
            User user = gson.fromJson(jsonUser, User.class);
            int update = userService.update(user);

            writer.print(update);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }
}

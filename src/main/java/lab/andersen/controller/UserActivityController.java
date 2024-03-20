package lab.andersen.controller;

import com.google.gson.Gson;
import lab.andersen.dao.UserActivityDao;
import lab.andersen.dto.enums.HttpMethods;
import lab.andersen.entity.UserActivity;
import lab.andersen.exception.ServiceException;
import lab.andersen.service.UserActivityService;
import lab.andersen.servlet.FrontController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;

public class UserActivityController extends FrontController {

    private final UserActivityService userActivityService = new UserActivityService(new UserActivityDao());
    private final Gson gson = new Gson();

    @Override
    public void process() throws IOException {
        response.setContentType("application/json");
        response.setContentType(StandardCharsets.UTF_8.name());
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
            response.setStatus(SC_METHOD_NOT_ALLOWED);
        }
    }

    private void handleGet(HttpServletRequest request, HttpServletResponse response, String[] splitPath) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            if (splitPath.length <= 2) {
                List<UserActivity> allUsersActivities = userActivityService.findAllUsersActivities();
                writer.write(gson.toJson(allUsersActivities));
            } else {
                int userActivityId = Integer.parseInt(splitPath[2]);
                UserActivity userActivity = userActivityService.findById(userActivityId);
                writer.write(gson.toJson(userActivity));
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
                userActivityService.delete(id);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to parse userActivityId to number");
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handlePost(HttpServletRequest request, HttpServletResponse response, String[] splitPath) throws IOException {
        try (
                BufferedReader reader = request.getReader();
                PrintWriter writer = response.getWriter()
        ) {
            String userActivityLines = reader.lines().collect(Collectors.joining());
            UserActivity user = gson.fromJson(userActivityLines, UserActivity.class);
            userActivityService.create(user);

            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private void handlePut(HttpServletRequest request, HttpServletResponse response, String[] splitPath) throws IOException {
        try (BufferedReader reader = request.getReader()) {
            String userActivityLines = reader.lines().collect(Collectors.joining());
            UserActivity user = gson.fromJson(userActivityLines, UserActivity.class);
            userActivityService.update(user);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }
}
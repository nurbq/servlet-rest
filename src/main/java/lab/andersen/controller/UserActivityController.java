package lab.andersen.controller;

import com.google.gson.Gson;
import lab.andersen.dao.UserActivityDao;
import lab.andersen.entity.UserActivity;
import lab.andersen.service.UserActivityService;
import lab.andersen.servlet.FrontController;
import lombok.SneakyThrows;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserActivityController extends FrontController {

    private final UserActivityService userActivityService = new UserActivityService(new UserActivityDao());
    private final Gson gson = new Gson();

    @SneakyThrows
    @Override
    public void process() {

        response.setContentType("application/json");
        response.setContentType(StandardCharsets.UTF_8.name());
        String method = request.getMethod();


        List<UserActivity> allUsersActivities = userActivityService.findAllUsersActivities();
        try (PrintWriter writer = response.getWriter()) {
            writer.print(gson.toJson(allUsersActivities));
        }
    }
}

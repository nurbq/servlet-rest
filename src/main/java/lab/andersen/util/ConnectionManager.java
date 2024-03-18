package lab.andersen.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;


@UtilityClass
public class ConnectionManager {

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String DB_DRIVER_KEY = "db.driver";

    static {
        loadDriver();
    }

    private static void loadDriver() {
        try {
            Class.forName(PropertiesUtils.get(DB_DRIVER_KEY));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static Connection open() {
        return DriverManager.getConnection(
                PropertiesUtils.get(URL_KEY),
                PropertiesUtils.get(USERNAME_KEY),
                PropertiesUtils.get(PASSWORD_KEY)
        );
    }
}

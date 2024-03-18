package lab.andersen.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.util.Properties;

@UtilityClass
public final class PropertiesUtils {

    public static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    @SneakyThrows
    private static void loadProperties() {
        InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties");
        PROPERTIES.load(inputStream);
    }
}

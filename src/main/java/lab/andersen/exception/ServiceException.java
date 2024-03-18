package lab.andersen.exception;

import java.sql.SQLException;

public class ServiceException extends SQLException {
    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String reason, Throwable cause) {
        super(reason, cause);
    }
}

package lab.andersen.exception;

import java.sql.SQLException;

public class DaoException extends SQLException {
    public DaoException(Throwable cause) {
        super(cause);
    }
}

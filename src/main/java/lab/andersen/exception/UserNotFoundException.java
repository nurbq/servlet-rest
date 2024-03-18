package lab.andersen.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String reason) {
        super(reason);
    }
}

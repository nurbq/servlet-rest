package lab.andersen.exception;

public class UserActivityNotFoundException extends RuntimeException{
    public UserActivityNotFoundException(String message) {
        super(message);
    }
}

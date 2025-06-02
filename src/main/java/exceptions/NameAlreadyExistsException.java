package exceptions;

public class NameAlreadyExistsException extends RuntimeException {
    public NameAlreadyExistsException(String userName) {
        super("There is already a user with the username " + userName);
    }
}

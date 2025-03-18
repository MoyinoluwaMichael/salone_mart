package africa.springCore.martbackend.infrastructure.exception;

public class UserNotFoundException extends MartException {
	public UserNotFoundException(String message) {
		super(message);
	}
}

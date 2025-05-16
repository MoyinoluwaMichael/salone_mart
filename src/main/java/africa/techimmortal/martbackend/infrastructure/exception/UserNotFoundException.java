package africa.techimmortal.martbackend.infrastructure.exception;

public class UserNotFoundException extends MartException {
	public UserNotFoundException(String message) {
		super(message);
	}
}

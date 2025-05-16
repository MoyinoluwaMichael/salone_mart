package africa.techimmortal.martbackend.infrastructure.exception;

public class UserAlreadyExistsException extends MartException {
	public UserAlreadyExistsException(String message) {
		super(message);
	}
}

package africa.techimmortal.martbackend.infrastructure.exception;

public class AuthenticationException extends MartException {
    public AuthenticationException(String message){
        super(message);
    }
}

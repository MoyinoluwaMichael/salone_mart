package africa.springCore.martbackend.infrastructure.security.manager;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import static africa.springCore.martbackend.common.Message.AUTHENTICATION_FAILED_FOR_USER_WITH_EMAIL;


@Component
@AllArgsConstructor
public class MartAuthenticationManager implements AuthenticationManager {
    private final AuthenticationProvider authenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication authResult;
        String email = (String) authentication.getPrincipal();
        if (authenticationProvider.supports(authentication.getClass())) {
            System.out.println("AuthenticationProvider Supported");
            authResult = authenticationProvider.authenticate(authentication);
            return authResult;
        } else throw new BadCredentialsException(String.format(AUTHENTICATION_FAILED_FOR_USER_WITH_EMAIL, email));
    }
}

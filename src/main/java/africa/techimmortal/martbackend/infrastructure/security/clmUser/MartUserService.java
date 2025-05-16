package africa.techimmortal.martbackend.infrastructure.security.clmUser;

import africa.techimmortal.martbackend.core.domain.model.BioData;
import africa.techimmortal.martbackend.core.domain.repository.BioDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import static africa.techimmortal.martbackend.core.utils.Message.INVALID_EMAIL_OR_PASSWORD;

@AllArgsConstructor
@Repository
public class MartUserService implements UserDetailsService {
    private final BioDataRepository bioDataRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BioData user = bioDataRepository.findByEmailAddress(username).orElseThrow(
                () ->
                        new UsernameNotFoundException(
                                INVALID_EMAIL_OR_PASSWORD
                        ));
        return new MartUser(user);
    }
}

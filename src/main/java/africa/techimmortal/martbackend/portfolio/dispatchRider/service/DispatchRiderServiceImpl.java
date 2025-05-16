package africa.techimmortal.martbackend.portfolio.dispatchRider.service;

import africa.techimmortal.martbackend.portfolio.dispatchRider.domain.model.DispatchRider;
import africa.techimmortal.martbackend.portfolio.dispatchRider.domain.repository.DispatchRiderRepository;
import africa.techimmortal.martbackend.infrastructure.exception.MapperException;
import africa.techimmortal.martbackend.infrastructure.exception.UserNotFoundException;
import africa.techimmortal.martbackend.core.utils.MartMapper;
import africa.techimmortal.martbackend.portfolio.dispatchRider.domain.dtos.responses.DispatchRiderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static africa.techimmortal.martbackend.core.utils.Message.USER_WITH_EMAIL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DispatchRiderServiceImpl implements DispatchRiderService {

    private final MartMapper martMapper;
    private final DispatchRiderRepository dispatchRiderRepository;

    @Override
    public DispatchRiderResponseDto findByEmail(String emailAddress) throws UserNotFoundException, MapperException {
        DispatchRider foundRider = dispatchRiderRepository.findByBioData_EmailAddress(emailAddress).orElseThrow(
                ()-> new UserNotFoundException(String.format(USER_WITH_EMAIL_NOT_FOUND, emailAddress))
        );
        return DispatchRiderResponseDto.parse(foundRider);
    }

    @Override
    public Long retrieveTotalTransporters() {
        return dispatchRiderRepository.count();
    }
}

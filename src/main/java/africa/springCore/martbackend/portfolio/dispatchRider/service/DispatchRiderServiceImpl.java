package africa.springCore.martbackend.portfolio.dispatchRider.service;

import africa.springCore.martbackend.core.portfolio.dispatchRider.domain.model.DispatchRider;
import africa.springCore.martbackend.core.portfolio.dispatchRider.domain.repository.DispatchRiderRepository;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.core.utils.MartMapper;
import africa.springCore.martbackend.portfolio.dispatchRider.domain.dtos.responses.DispatchRiderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static africa.springCore.martbackend.core.utils.Message.USER_WITH_EMAIL_NOT_FOUND;

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

package africa.springCore.martbackend.portfolio.dispatchRider.service;

import africa.springCore.martbackend.core.base.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.core.portfolio.dispatchRider.domain.model.DispatchRider;
import africa.springCore.martbackend.core.portfolio.dispatchRider.domain.repository.DispatchRiderRepository;
import africa.springCore.martbackend.core.portfolio.dispatchRider.domain.dtos.responses.DispatchRiderResponseDto;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.common.utils.MartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static africa.springCore.martbackend.common.Message.USER_WITH_EMAIL_NOT_FOUND;

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
        BioDataResponseDto bioDataResponse = martMapper.readValue(foundRider.getBioData(), BioDataResponseDto.class);
        DispatchRiderResponseDto dispatchRiderResponseDto = martMapper.readValue(foundRider, DispatchRiderResponseDto.class);
        dispatchRiderResponseDto.setBioData(bioDataResponse);
        return dispatchRiderResponseDto;
    }
}

package africa.techimmortal.martbackend.core.service;

import africa.techimmortal.martbackend.core.domain.dtos.response.BioDataResponseDto;
import africa.techimmortal.martbackend.core.domain.repository.BioDataRepository;
import africa.techimmortal.martbackend.core.domain.model.BioData;
import africa.techimmortal.martbackend.infrastructure.exception.MapperException;
import africa.techimmortal.martbackend.infrastructure.exception.UserAlreadyExistsException;
import africa.techimmortal.martbackend.infrastructure.exception.UserNotFoundException;
import africa.techimmortal.martbackend.core.utils.MartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static africa.techimmortal.martbackend.core.utils.Message.*;

@Service
@RequiredArgsConstructor
public class BioDataServiceImpl implements BioDataService {

    private final MartMapper martMapper;
    private final BioDataRepository bioDataRepository;

    @Override
    public BioDataResponseDto findByEmail(String email) throws UserNotFoundException, MapperException {
        BioData foundBioData = bioDataRepository.findByEmailAddress(email).orElseThrow(
                ()-> new UserNotFoundException(String.format(USER_WITH_EMAIL_NOT_FOUND, email))
        );
        return BioDataResponseDto.parse(foundBioData);
    }


    @Override
    public void validateDuplicateUserExistence(String emailAddress) throws UserAlreadyExistsException {
        var bioData = bioDataRepository.findByEmailAddress(emailAddress);
        boolean accountWithGivenEmailAlreadyExist = bioData.isPresent();
        if (accountWithGivenEmailAlreadyExist) throw new UserAlreadyExistsException(String.format(ACCOUNT_ALREADY_EXIST, emailAddress));
    }

    @Override
    public BioDataResponseDto findByPhoneNumber(String phoneNumber) throws UserNotFoundException, MapperException {
        BioData foundBioData = bioDataRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                ()-> new UserNotFoundException(String.format(USER_WITH_PHONE_NUMBER_NOT_FOUND, phoneNumber))
        );
        return BioDataResponseDto.parse(foundBioData);
    }

}

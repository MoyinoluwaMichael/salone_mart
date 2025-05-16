package africa.techimmortal.martbackend.core.service;

import africa.techimmortal.martbackend.core.domain.dtos.response.BioDataResponseDto;
import africa.techimmortal.martbackend.infrastructure.exception.MapperException;
import africa.techimmortal.martbackend.infrastructure.exception.UserAlreadyExistsException;
import africa.techimmortal.martbackend.infrastructure.exception.UserNotFoundException;

public interface BioDataService {
    BioDataResponseDto findByEmail(String email) throws UserNotFoundException, MapperException;

    void validateDuplicateUserExistence(String emailAddress) throws UserAlreadyExistsException;

    BioDataResponseDto findByPhoneNumber(String phoneNumber) throws UserNotFoundException, MapperException;

}

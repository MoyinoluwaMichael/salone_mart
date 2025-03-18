package africa.springCore.martbackend.core.base.service;

import africa.springCore.martbackend.core.base.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserAlreadyExistsException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;

public interface BioDataService {
    BioDataResponseDto findByEmail(String email) throws UserNotFoundException, MapperException;

    void validateDuplicateUserExistence(String emailAddress) throws UserAlreadyExistsException;

    BioDataResponseDto findByPhoneNumber(String phoneNumber) throws UserNotFoundException, MapperException;

}

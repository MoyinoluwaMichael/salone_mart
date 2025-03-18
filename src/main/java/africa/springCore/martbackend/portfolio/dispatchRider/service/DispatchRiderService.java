package africa.springCore.martbackend.portfolio.dispatchRider.service;

import africa.springCore.martbackend.core.portfolio.dispatchRider.domain.dtos.responses.DispatchRiderResponseDto;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;

public interface DispatchRiderService {
    DispatchRiderResponseDto findByEmail(String emailAddress) throws UserNotFoundException, MapperException;
}

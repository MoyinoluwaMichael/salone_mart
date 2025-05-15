package africa.springCore.martbackend.portfolio.dispatchRider.service;

import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.UserNotFoundException;
import africa.springCore.martbackend.portfolio.dispatchRider.domain.dtos.responses.DispatchRiderResponseDto;

public interface DispatchRiderService {
    DispatchRiderResponseDto findByEmail(String emailAddress) throws UserNotFoundException, MapperException;

    Long retrieveTotalTransporters();
}

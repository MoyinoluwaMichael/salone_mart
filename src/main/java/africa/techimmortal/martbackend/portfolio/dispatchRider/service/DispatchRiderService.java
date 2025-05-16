package africa.techimmortal.martbackend.portfolio.dispatchRider.service;

import africa.techimmortal.martbackend.infrastructure.exception.MapperException;
import africa.techimmortal.martbackend.infrastructure.exception.UserNotFoundException;
import africa.techimmortal.martbackend.portfolio.dispatchRider.domain.dtos.responses.DispatchRiderResponseDto;

public interface DispatchRiderService {
    DispatchRiderResponseDto findByEmail(String emailAddress) throws UserNotFoundException, MapperException;

    Long retrieveTotalTransporters();
}

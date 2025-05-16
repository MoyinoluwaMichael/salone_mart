package africa.techimmortal.martbackend.core.service;

import africa.techimmortal.martbackend.core.domain.enums.Role;
import africa.techimmortal.martbackend.core.domain.dtos.request.AddressCreationRequest;
import africa.techimmortal.martbackend.core.domain.dtos.response.AddressListingDto;
import africa.techimmortal.martbackend.core.domain.dtos.response.AddressResponseDto;
import africa.techimmortal.martbackend.infrastructure.exception.AddressNotFoundException;
import africa.techimmortal.martbackend.infrastructure.exception.MapperException;
import org.springframework.data.domain.Pageable;

public interface AddressService {
    AddressResponseDto createUserAddress(AddressCreationRequest addressCreationRequest, Role userType, Long userId) throws MapperException;

    AddressResponseDto findById(Long id, Role userType) throws MapperException, AddressNotFoundException;

    AddressListingDto findByUserId(Long userId, Role customer, Pageable pageable);

    AddressResponseDto updateUserAddress(AddressCreationRequest addressCreationRequest, Long customerId, Role userType, Long addressId) throws AddressNotFoundException, MapperException;
}

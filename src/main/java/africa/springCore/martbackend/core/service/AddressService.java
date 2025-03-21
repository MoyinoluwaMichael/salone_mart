package africa.springCore.martbackend.core.service;

import africa.springCore.martbackend.core.domain.enums.Role;
import africa.springCore.martbackend.core.domain.dtos.request.AddressCreationRequest;
import africa.springCore.martbackend.core.domain.dtos.response.AddressListingDto;
import africa.springCore.martbackend.core.domain.dtos.response.AddressResponseDto;
import africa.springCore.martbackend.infrastructure.exception.AddressNotFoundException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import org.springframework.data.domain.Pageable;

public interface AddressService {
    AddressResponseDto createUserAddress(AddressCreationRequest addressCreationRequest, Role userType, Long userId) throws MapperException;

    AddressResponseDto findById(Long id, Role userType) throws MapperException, AddressNotFoundException;

    AddressListingDto findByUserId(Long userId, Role customer, Pageable pageable);

    AddressResponseDto updateUserAddress(AddressCreationRequest addressCreationRequest, Long customerId, Role userType, Long addressId) throws AddressNotFoundException, MapperException;
}

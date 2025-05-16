package africa.techimmortal.martbackend.core.service;

import africa.techimmortal.martbackend.core.domain.enums.Role;
import africa.techimmortal.martbackend.core.utils.MartMapper;
import africa.techimmortal.martbackend.core.domain.dtos.request.AddressCreationRequest;
import africa.techimmortal.martbackend.core.domain.dtos.response.AddressListingDto;
import africa.techimmortal.martbackend.core.domain.dtos.response.AddressResponseDto;
import africa.techimmortal.martbackend.core.domain.model.Address;
import africa.techimmortal.martbackend.core.domain.repository.AddressRepository;
import africa.techimmortal.martbackend.infrastructure.exception.AddressNotFoundException;
import africa.techimmortal.martbackend.infrastructure.exception.MapperException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static africa.techimmortal.martbackend.core.utils.Message.USER_ADDRESS_WITH_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final MartMapper martMapper;

    @Override
    public AddressResponseDto createUserAddress(AddressCreationRequest addressCreationRequest, Role userType, Long userId) throws MapperException {
        Address address = martMapper.readValue(addressCreationRequest, Address.class);
        address.setUserType(userType);
        address.setUserId(userId);
        return this.getAddressCreationResponse(addressRepository.save(address));
    }

    @Override
    public AddressResponseDto findById(Long id, Role userType) throws MapperException, AddressNotFoundException {
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new AddressNotFoundException(String.format(USER_ADDRESS_WITH_ID_NOT_FOUND, userType, id))
        );
        return getAddressCreationResponse(address);
    }

    @Override
    public AddressListingDto findByUserId(Long userId, Role customer, Pageable pageable) {
        Page<AddressResponseDto> pagedAddresses = addressRepository.findByUserIdAndUserType(userId, customer, pageable).map((address) -> {
            try {
                return martMapper.readValue(address, AddressResponseDto.class);
            } catch (MapperException e) {
                e.printStackTrace();
            }
            return null;
        });
        return getAddressListingDto(pagedAddresses, pageable);
    }

    @Override
    public AddressResponseDto updateUserAddress(AddressCreationRequest addressCreationRequest, Long customerId, Role userType, Long addressId) throws AddressNotFoundException, MapperException {
        findById(addressId, userType);
        Address address = addressRepository.findById(addressId).get();
        if (addressCreationRequest.getAddressType() != null)
            address.setAddressType(addressCreationRequest.getAddressType());
        if (addressCreationRequest.getNumber() > 0) address.setNumber(addressCreationRequest.getNumber());
        if (addressCreationRequest.getStreetName() != null && !StringUtils.isEmpty(addressCreationRequest.getStreetName()))
            address.setStreetName(addressCreationRequest.getStreetName());
        if (addressCreationRequest.getNearestBusStop() != null && !StringUtils.isEmpty(addressCreationRequest.getNearestBusStop()))
            address.setNearestBusStop(addressCreationRequest.getNearestBusStop());
        if (addressCreationRequest.getCity() != null && !StringUtils.isEmpty(addressCreationRequest.getCity()))
            address.setCity(addressCreationRequest.getCity());
        if (addressCreationRequest.getState() != null && !StringUtils.isEmpty(addressCreationRequest.getState()))
            address.setState(addressCreationRequest.getState());
        if (addressCreationRequest.getLocalGovernmentArea() != null && !StringUtils.isEmpty(addressCreationRequest.getLocalGovernmentArea()))
            address.setLocalGovernmentArea(addressCreationRequest.getLocalGovernmentArea());
        if (addressCreationRequest.getCountry() != null && !StringUtils.isEmpty(addressCreationRequest.getCountry()))
            address.setCountry(addressCreationRequest.getCountry());
        return getAddressCreationResponse(addressRepository.save(address));
    }

    private AddressListingDto getAddressListingDto(Page<AddressResponseDto> pagedCustomers, Pageable pageable) {
        AddressListingDto addressListingDto = new AddressListingDto();
        addressListingDto.setAddresses(pagedCustomers.getContent());
        addressListingDto.setPageNumber(pageable.getPageNumber());
        addressListingDto.setPageSize(pageable.getPageSize());
        return addressListingDto;
    }

    private AddressResponseDto getAddressCreationResponse(Address address) throws MapperException {
        return martMapper.readValue(address, AddressResponseDto.class);
    }
}

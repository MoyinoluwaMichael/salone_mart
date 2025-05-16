package africa.techimmortal.martbackend.portfolio.customer.service;

import africa.techimmortal.martbackend.portfolio.customer.domain.dtos.requests.CustomerCreationRequest;
import africa.techimmortal.martbackend.portfolio.customer.domain.dtos.requests.CustomerUpdateRequest;
import africa.techimmortal.martbackend.portfolio.customer.exception.CustomerCreationFailedException;
import africa.techimmortal.martbackend.infrastructure.exception.MartException;
import africa.techimmortal.martbackend.infrastructure.exception.MapperException;
import africa.techimmortal.martbackend.infrastructure.exception.UserNotFoundException;
import africa.techimmortal.martbackend.infrastructure.exception.MediaUploadFailedException;
import africa.techimmortal.martbackend.portfolio.customer.domain.dtos.responses.CustomerListingDto;
import africa.techimmortal.martbackend.portfolio.customer.domain.dtos.responses.CustomerResponseDto;
import africa.techimmortal.martbackend.portfolio.customer.exception.CustomerUpdateFailedException;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerResponseDto createCustomer(CustomerCreationRequest customerCreationRequest) throws MartException, CustomerCreationFailedException;

    CustomerResponseDto findByEmail(String email) throws UserNotFoundException, MapperException;
    CustomerResponseDto findById(Long id) throws UserNotFoundException, MapperException;

    CustomerListingDto retrieveAll(Pageable pageable) throws MapperException;

    CustomerListingDto searchBy(String searchParam, String value, Pageable pageable);

    CustomerResponseDto updateCustomer(Long id, CustomerUpdateRequest customerUpdateRequest) throws CustomerCreationFailedException, UserNotFoundException, MapperException, CustomerUpdateFailedException, MediaUploadFailedException;

    Long retrieveTotalCustomers();
}

package africa.springCore.martbackend.portfolio.admin.service;

import africa.springCore.martbackend.common.data.ApiResponse;
import africa.springCore.martbackend.core.portfolio.admin.domain.dtos.requests.AdminInvitationRequest;
import africa.springCore.martbackend.core.portfolio.admin.exception.AdminNotFoundException;
import africa.springCore.martbackend.core.portfolio.admin.exception.AdminUpdateFailedException;
import africa.springCore.martbackend.core.portfolio.customer.exception.CustomerCreationFailedException;
import africa.springCore.martbackend.infrastructure.exception.*;
import africa.springCore.martbackend.portfolio.admin.domain.dtos.requests.AdminUpdateRequest;
import africa.springCore.martbackend.portfolio.admin.domain.dtos.responses.AdminListingDto;
import africa.springCore.martbackend.portfolio.admin.domain.dtos.responses.AdminResponseDto;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    ApiResponse sendInvitationLink(String emailAddress) throws MartException;

    AdminResponseDto findByEmail(String emailAddress) throws MapperException, UserNotFoundException;

    ApiResponse acceptInvitation(String encryptedLink, AdminInvitationRequest request) throws MartException;

    ApiResponse validateToken(String token);

    AdminListingDto findAll(Pageable pageable);

    AdminResponseDto findById(Long id) throws AdminNotFoundException, MapperException;

    AdminResponseDto updateAdmin(Long id, AdminUpdateRequest adminUpdateRequest) throws AdminNotFoundException, MapperException, AdminUpdateFailedException, UserNotFoundException, CustomerCreationFailedException;
}

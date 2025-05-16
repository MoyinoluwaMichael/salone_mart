package africa.techimmortal.martbackend.portfolio.admin.service;

import africa.techimmortal.martbackend.core.domain.dtos.response.ApiResponse;
import africa.techimmortal.martbackend.infrastructure.exception.MapperException;
import africa.techimmortal.martbackend.infrastructure.exception.MartException;
import africa.techimmortal.martbackend.infrastructure.exception.UserNotFoundException;
import africa.techimmortal.martbackend.portfolio.admin.domain.dtos.requests.AdminInvitationRequest;
import africa.techimmortal.martbackend.portfolio.admin.exception.AdminNotFoundException;
import africa.techimmortal.martbackend.portfolio.admin.exception.AdminUpdateFailedException;
import africa.techimmortal.martbackend.portfolio.customer.exception.CustomerCreationFailedException;
import africa.techimmortal.martbackend.portfolio.admin.domain.dtos.requests.AdminUpdateRequest;
import africa.techimmortal.martbackend.portfolio.admin.domain.dtos.responses.AdminDashboardResponse;
import africa.techimmortal.martbackend.portfolio.admin.domain.dtos.responses.AdminListingDto;
import africa.techimmortal.martbackend.portfolio.admin.domain.dtos.responses.AdminResponseDto;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    ApiResponse sendInvitationLink(String emailAddress) throws MartException;

    AdminResponseDto findByEmail(String emailAddress) throws MapperException, UserNotFoundException;

    ApiResponse acceptInvitation(String encryptedLink, AdminInvitationRequest request) throws MartException;
    AdminDashboardResponse retrieveAdminDashboard(Pageable pageable);

    ApiResponse validateToken(String token);

    AdminListingDto findAll(Pageable pageable);

    AdminResponseDto findById(Long id) throws AdminNotFoundException, MapperException;

    AdminResponseDto updateAdmin(Long id, AdminUpdateRequest adminUpdateRequest) throws AdminNotFoundException, MapperException, AdminUpdateFailedException, UserNotFoundException, CustomerCreationFailedException;
}

package africa.springCore.martbackend.portfolio.vendor.domain.dtos.requests;

import africa.springCore.martbackend.portfolio.user.domain.dto.FileMetaData;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class VendorCreationRequest {

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Email address is mandatory")
    @Email(message = "Email address should be valid")
    private String emailAddress;

    private String firstName;
    private String businessName;

    private String lastName;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    private String phoneNumber;

}

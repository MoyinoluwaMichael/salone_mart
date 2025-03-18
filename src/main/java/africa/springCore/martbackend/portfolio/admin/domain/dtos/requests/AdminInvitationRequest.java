package africa.springCore.martbackend.core.portfolio.admin.domain.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminInvitationRequest {
    @NotBlank(message = "firstName must not be empty")
    private String firstName;
    @NotBlank(message = "lastName must not be empty")
    private String lastName;
    @NotBlank(message = "phoneNumber must not be empty")
    private String phoneNumber;
}

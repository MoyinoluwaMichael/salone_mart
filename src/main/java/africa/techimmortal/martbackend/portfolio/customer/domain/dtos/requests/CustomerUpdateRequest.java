package africa.techimmortal.martbackend.portfolio.customer.domain.dtos.requests;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerUpdateRequest {

    private String emailAddress;

    private String firstName;

    private String lastName;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    private String phoneNumber;
}

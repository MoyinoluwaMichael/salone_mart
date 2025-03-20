package africa.springCore.martbackend.portfolio.vendor.domain.dtos.requests;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class VendorUpdateRequest {

    private String emailAddress;

    private String firstName;

    private String lastName;
    private String businessName;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    private String phoneNumber;

    private List<FileMetaData> files;
}

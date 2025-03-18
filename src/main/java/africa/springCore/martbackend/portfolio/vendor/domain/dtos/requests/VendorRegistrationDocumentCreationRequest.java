package africa.springCore.martbackend.core.portfolio.vendor.domain.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VendorRegistrationDocumentCreationRequest {

    private String name;

    private String referenceUrl;
}

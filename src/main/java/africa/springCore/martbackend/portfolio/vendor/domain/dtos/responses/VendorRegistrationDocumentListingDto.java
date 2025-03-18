package africa.springCore.martbackend.core.portfolio.vendor.domain.dtos.responses;

import africa.springCore.martbackend.common.data.BasePageableResponse;
import africa.springCore.martbackend.core.portfolio.vendor.domain.model.VendorRegistrationDocument;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class VendorRegistrationDocumentListingDto extends BasePageableResponse {
    private List<VendorRegistrationDocument> vendorRegistrationDocuments;
}

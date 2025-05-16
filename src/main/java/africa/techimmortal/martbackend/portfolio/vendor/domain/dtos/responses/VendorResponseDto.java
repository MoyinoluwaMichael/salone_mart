package africa.techimmortal.martbackend.portfolio.vendor.domain.dtos.responses;

import africa.techimmortal.martbackend.core.domain.enums.ApprovalStatus;
import africa.techimmortal.martbackend.core.domain.dtos.response.BioDataResponseDto;
import africa.techimmortal.martbackend.portfolio.vendor.domain.model.Vendor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VendorResponseDto {

    private Long id;
    private BioDataResponseDto bioData;
    private String businessName;
    private ApprovalStatus status;
    private String category;
    private String createdAt;

    public static VendorResponseDto parse(Vendor foundVendor) {
        VendorResponseDto vendorResponseDto = new VendorResponseDto();
        vendorResponseDto.setId(foundVendor.getId());
        vendorResponseDto.setBusinessName(foundVendor.getBusinessName());
        vendorResponseDto.setStatus(foundVendor.getStatus());
        vendorResponseDto.setCategory(foundVendor.getCategory());
        vendorResponseDto.setCreatedAt(foundVendor.getCreatedAt().toString());
        vendorResponseDto.setBioData(BioDataResponseDto.parse(foundVendor.getBioData()));
        return vendorResponseDto;
    }
}

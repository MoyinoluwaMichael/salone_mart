package africa.springCore.martbackend.portfolio.vendor.domain.dtos.responses;

import africa.springCore.martbackend.core.domain.enums.ApprovalStatus;
import africa.springCore.martbackend.core.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.portfolio.system.domain.model.Media;
import africa.springCore.martbackend.portfolio.vendor.domain.model.Vendor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

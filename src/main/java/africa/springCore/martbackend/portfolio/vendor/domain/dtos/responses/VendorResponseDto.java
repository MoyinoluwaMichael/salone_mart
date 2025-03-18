package africa.springCore.martbackend.portfolio.vendor.domain.dtos.responses;

import africa.springCore.martbackend.common.enums.ApprovalStatus;
import africa.springCore.martbackend.core.base.domain.dtos.response.BioDataResponseDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VendorResponseDto {

    private Long id;
    private BioDataResponseDto bioData;
    private String businessName;

    private ApprovalStatus approvalStatus;
}

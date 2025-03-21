package africa.springCore.martbackend.portfolio.vendor.domain.dtos.responses;

import africa.springCore.martbackend.core.domain.enums.ApprovalStatus;
import africa.springCore.martbackend.core.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.portfolio.user.domain.model.Media;
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
    private List<Media> media = new ArrayList<>();
}

package africa.springCore.martbackend.portfolio.vendor.domain.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class VendorListingDto {
    private int pageNumber;
    private int pageSize;
    private List<VendorResponseDto> vendors;
}

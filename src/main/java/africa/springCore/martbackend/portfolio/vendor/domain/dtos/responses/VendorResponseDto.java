package africa.springCore.martbackend.portfolio.vendor.domain.dtos.responses;

import africa.springCore.martbackend.core.domain.enums.ApprovalStatus;
import africa.springCore.martbackend.core.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.portfolio.product.domain.model.ProductCategory;
import africa.springCore.martbackend.portfolio.system.domain.model.Media;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class VendorResponseDto {

    private Long id;
    private BioDataResponseDto bioData;
    private String businessName;
    private ApprovalStatus status;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ProductCategory category;
    private List<Media> media = new ArrayList<>();
    private String createdAt;
}

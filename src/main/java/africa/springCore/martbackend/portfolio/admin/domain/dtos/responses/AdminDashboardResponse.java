package africa.springCore.martbackend.portfolio.admin.domain.dtos.responses;

import africa.springCore.martbackend.portfolio.product.domain.dtos.response.CategoryProductCountDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class AdminDashboardResponse {
    private Long totalOrders;
    private Long totalCustomers;
    private Long totalProducts;
    private Long totalTransporters;
    private Long totalVendors;
    List<CategoryProductCountDto> categoryProductCount;
}

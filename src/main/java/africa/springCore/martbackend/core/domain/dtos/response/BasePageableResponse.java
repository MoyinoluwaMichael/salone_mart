package africa.springCore.martbackend.core.domain.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
public class BasePageableResponse<T> {
    private int pageNumber;
    private int pageSize;
    private int totalFilteredItems;
    private Long rowSize;
    private List<T> data;

    public static <T> BasePageableResponse<T> instance(Page<T> pagedData) {
        BasePageableResponse<T> response = new BasePageableResponse<>();
        response.setPageNumber(pagedData.getNumber());
        response.setPageSize(pagedData.getSize());
        response.setRowSize(pagedData.getTotalElements());
        response.setTotalFilteredItems(pagedData.getContent().size());
        response.setData(pagedData.getContent());
        return response;
    }
}

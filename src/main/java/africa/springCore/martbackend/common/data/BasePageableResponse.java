package africa.springCore.martbackend.common.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BasePageableResponse {
    private int pageNumber;
    private int pageSize;
}

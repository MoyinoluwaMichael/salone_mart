package africa.springCore.martbackend.portfolio.system.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileMetaData {

    private String id;
    private Long documentTypeId;
    private String mediaCategory;

}

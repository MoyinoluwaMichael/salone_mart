package africa.springCore.martbackend.portfolio.admin.domain.dtos.responses;

import africa.springCore.martbackend.core.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.portfolio.admin.domain.model.Admin;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminResponseDto {

    private Long id;
    private BioDataResponseDto bioData;

    public static AdminResponseDto parse(Admin foundAdmin) {
        AdminResponseDto adminResponseDto = new AdminResponseDto();
        adminResponseDto.setId(foundAdmin.getId());
        adminResponseDto.setBioData(BioDataResponseDto.parse(foundAdmin.getBioData()));
        return adminResponseDto;
    }
}

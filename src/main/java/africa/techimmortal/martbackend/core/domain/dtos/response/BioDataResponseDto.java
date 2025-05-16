package africa.techimmortal.martbackend.core.domain.dtos.response;

import africa.techimmortal.martbackend.core.domain.enums.Role;
import africa.techimmortal.martbackend.core.domain.model.BioData;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class BioDataResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private String displayPicture;
    private List<Role> roles;
    private Boolean isEnabled;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;

    public static BioDataResponseDto parse(BioData foundBioData) {
        BioDataResponseDto responseDto = new BioDataResponseDto();
        responseDto.setId(foundBioData.getId());
        responseDto.setFirstName(foundBioData.getFirstName());
        responseDto.setLastName(foundBioData.getLastName());
        responseDto.setEmailAddress(foundBioData.getEmailAddress());
        responseDto.setPhoneNumber(foundBioData.getPhoneNumber());
        responseDto.setRoles(foundBioData.getRoles());
        responseDto.setIsEnabled(foundBioData.getIsEnabled());
        responseDto.setCreatedAt(foundBioData.getCreatedAt());
        responseDto.setDisplayPicture(foundBioData.getDisplayPicture() == null ? null : foundBioData.getDisplayPicture().getSecureUrl());
        return responseDto;
    }
}

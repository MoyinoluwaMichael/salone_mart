package africa.springCore.martbackend.core.domain.dtos.response;

import africa.springCore.martbackend.core.domain.enums.Role;
import africa.springCore.martbackend.portfolio.system.domain.model.Media;
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
    private List<Media> media;
    private List<Role> roles;
    private Boolean isEnabled;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;
}

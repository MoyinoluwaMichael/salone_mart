package africa.springCore.martbackend.portfolio.admin.domain.model;

import africa.springCore.martbackend.core.base.domain.model.BaseEntity;
import africa.springCore.martbackend.core.base.domain.model.BioData;
import africa.springCore.martbackend.core.base.domain.model.MediaType;
import africa.springCore.martbackend.infrastructure.cloudservice.storageservice.service.CloudinaryUploadService;
import africa.springCore.martbackend.infrastructure.exception.UserUpdateFailedException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;

@Entity
@Table(name = "admin")
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor(staticName = "instance", access = AccessLevel.PRIVATE)
public class Admin extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7466640123337613601L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BioData bioData;

    public Admin uploadAndAddMedia(MultipartFile file, CloudinaryUploadService cloudinaryUploadService, String mediaPurpose, MediaType mediaType) throws UserUpdateFailedException {
        BioData bioData = this.bioData.uploadAndAddMedia(file, cloudinaryUploadService, mediaPurpose, mediaType);
        this.setBioData(bioData);
        return this;
    }
}

package africa.techimmortal.martbackend.portfolio.vendor.domain.model;

import africa.techimmortal.martbackend.core.domain.enums.ApprovalStatus;
import africa.techimmortal.martbackend.core.domain.model.BaseEntity;
import africa.techimmortal.martbackend.core.domain.model.BioData;
import africa.techimmortal.martbackend.infrastructure.cloudservice.storageservice.service.CloudinaryUploadService;
import africa.techimmortal.martbackend.infrastructure.exception.MediaUploadFailedException;
import africa.techimmortal.martbackend.portfolio.system.domain.model.Media;
import africa.techimmortal.martbackend.portfolio.system.domain.model.MediaCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.util.Map;

@Entity
@Table(name = "vendor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "instance", access = AccessLevel.PRIVATE)
public class Vendor extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7466640123337613601L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BioData bioData;

    @Column(name = "category")
    private String category;

    public Media uploadAndAddMedia(MultipartFile file, CloudinaryUploadService cloudinaryUploadService, MediaCategory mediaCategory, String folderName, String documentType) throws MediaUploadFailedException {
        Map<String, Object> uploadResponse;
        try {
            uploadResponse = cloudinaryUploadService.uploadFile(file, folderName);
        } catch (Exception e) {
            throw new MediaUploadFailedException("User image upload failed: "+ e.getMessage());
        }
        if (uploadResponse.containsKey("error")) {
            throw new MediaUploadFailedException("User image upload failed");
        }

        String publicId = (String) uploadResponse.get("public_id");
        String secureUrl = (String) uploadResponse.get("secure_url");
        return Media.userInstance(mediaCategory, documentType, publicId, secureUrl, file, bioData.getId());
    }
}

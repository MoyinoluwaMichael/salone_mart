package africa.springCore.martbackend.core.domain.model;

import africa.springCore.martbackend.core.domain.enums.Role;
import africa.springCore.martbackend.infrastructure.cloudservice.storageservice.service.CloudinaryUploadService;
import africa.springCore.martbackend.infrastructure.exception.MediaUploadFailedException;
import africa.springCore.martbackend.portfolio.user.domain.model.DocumentType;
import africa.springCore.martbackend.portfolio.user.domain.model.Media;
import africa.springCore.martbackend.portfolio.user.domain.model.MediaCategory;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter

@Table(name = "bio_data", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email_address"),
        @UniqueConstraint(columnNames = "phone_number")
})
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@ToString
public class BioData implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Email
    @NotBlank
    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(name = "phone_number", nullable = true, unique = true)
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Media> media = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    @CreatedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    private Boolean isEnabled;

    public void addMedia(Media media) {
        this.media.add(media);
    }

    public BioData removeMedia(Media media, CloudinaryUploadService cloudinaryUploadService) throws MediaUploadFailedException {
        this.media.remove(media);
        try {
            cloudinaryUploadService.deleteFile(media.getPublicId());
        } catch (Exception e) {
            throw new MediaUploadFailedException(e.getMessage());
        }
        return this;
    }

    public Media getMediaByDocumentTypeId(Long typeId) {
        return this.media.stream()
                .filter(m -> typeId.equals(m.getDocumentType().getId()))
                .findFirst()
                .orElse(null);
    }

    public List<Media> getMediaByType(MediaCategory type) {
        return this.media.stream()
                .filter(m -> type.equals(m.getType()))
                .collect(Collectors.toList());
    }

    public List<Media> getDocuments() {
        return getMediaByType(MediaCategory.DOCUMENT);
    }

    public BioData uploadAndAddMedia(MultipartFile file, CloudinaryUploadService cloudinaryUploadService, MediaCategory mediaCategory, String folderName, DocumentType documentType) throws MediaUploadFailedException {
        Map<String, Object> uploadResponse = new HashMap<>();
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
        Media media = Media.userInstance(mediaCategory, documentType, publicId, secureUrl, file);
        this.addMedia(media);
        return this;
    }

}

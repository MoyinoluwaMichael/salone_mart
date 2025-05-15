package africa.springCore.martbackend.portfolio.system.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "media")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Media implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MediaCategory type;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "document_type")
    private String documentType; // e.g., "PROFILE_PICTURE", "ID_DOCUMENT", "CERTIFICATE"

    @Column(name = "public_id")
    private String publicId; // Cloudinary public_id

    @Column(name = "secure_url")
    private String secureUrl; // Cloudinary secure_url

    @Column(name = "file_name")
    private String fileName; // Original file name

    @Column(name = "file_type")
    private String fileType; // MIME type

    @Column(name = "file_size")
    private Long fileSize; // Size in bytes

    @Column(name = "created_at")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt;

    public static Media userInstance(MediaCategory mediaCategory, String documentName, String publicId, String secureUrl, MultipartFile file, Long ownerId) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        return new Media(null, mediaCategory, ownerId, null, documentName, publicId, secureUrl, file.getOriginalFilename(), file.getContentType(), file.getSize(), LocalDateTime.now(), LocalDateTime.now());
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

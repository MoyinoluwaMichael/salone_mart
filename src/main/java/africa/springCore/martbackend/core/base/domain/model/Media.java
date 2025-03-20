package africa.springCore.martbackend.core.base.domain.model;

import africa.springCore.martbackend.portfolio.product.domain.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
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
    private MediaType type; // PICTURE or DOCUMENT

    @Column(name = "media_purpose")
    private String purpose; // e.g., "PROFILE_PICTURE", "ID_DOCUMENT", "CERTIFICATE"

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
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bio_data_id")
    private BioData bioData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public static Media productInstance(MediaType mediaType, String type, String publicId, String secureUrl, MultipartFile file, Product savedProduct) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        return new Media(null, mediaType, type, publicId, secureUrl, file.getOriginalFilename(), file.getContentType(), file.getSize(), LocalDateTime.now(), LocalDateTime.now(), null, savedProduct);
    }


    public static Media userInstance(MediaType mediaType, String type, String publicId, String secureUrl, MultipartFile file, BioData savedUser) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        return new Media(null, mediaType, type, publicId, secureUrl, file.getOriginalFilename(), file.getContentType(), file.getSize(), LocalDateTime.now(), LocalDateTime.now(), savedUser, null);
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

    public void setUser(BioData bioData) {
        this.bioData = bioData;
    }
}


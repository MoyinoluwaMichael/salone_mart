package africa.springCore.martbackend.portfolio.product.domain.model;

import africa.springCore.martbackend.core.domain.model.BaseEntity;
import africa.springCore.martbackend.infrastructure.cloudservice.storageservice.service.CloudinaryUploadService;
import africa.springCore.martbackend.infrastructure.exception.MediaUploadFailedException;
import africa.springCore.martbackend.portfolio.system.domain.model.Media;
import africa.springCore.martbackend.portfolio.system.domain.model.MediaCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor(staticName = "instance", access = AccessLevel.PRIVATE)
public class Product extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7466640123337613601L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "discounted_price")
    private BigDecimal discountedPrice;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @JoinColumn(name = "interest")
    @OneToOne(cascade = CascadeType.ALL)
    private ProductInterest interest;

    public void setInterest(Long interest) {
        this.interest = ProductInterest.instanceOf(true, interest);
    }

    public Media uploadAndAddMedia(MultipartFile file, CloudinaryUploadService cloudinaryUploadService, MediaCategory mediaCategory, String folderName, String documentType) throws MediaUploadFailedException {
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
        return Media.userInstance(mediaCategory, documentType, publicId, secureUrl, file, id);
    }
}

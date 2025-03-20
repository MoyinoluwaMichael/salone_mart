package africa.springCore.martbackend.portfolio.customer.domain.model;

import africa.springCore.martbackend.core.base.domain.model.BaseEntity;
import africa.springCore.martbackend.core.base.domain.model.BioData;
import africa.springCore.martbackend.core.base.domain.model.Media;
import africa.springCore.martbackend.core.base.domain.model.MediaType;
import africa.springCore.martbackend.infrastructure.cloudservice.storageservice.service.CloudinaryUploadService;
import africa.springCore.martbackend.infrastructure.exception.UserUpdateFailedException;
import africa.springCore.martbackend.portfolio.customer.exception.CustomerUpdateFailedException;
import africa.springCore.martbackend.portfolio.product.domain.model.Product;
import africa.springCore.martbackend.portfolio.product.exception.ProductCreationFailedException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "instance", access = AccessLevel.PRIVATE)
public class Customer extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -7466640123337613601L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BioData bioData;


    public Customer uploadAndAddMedia(MultipartFile file, CloudinaryUploadService cloudinaryUploadService, String mediaPurpose, MediaType mediaType) throws UserUpdateFailedException {
        BioData bioData = this.bioData.uploadAndAddMedia(file, cloudinaryUploadService, mediaPurpose, mediaType);
        this.setBioData(bioData);
        return this;
    }

}

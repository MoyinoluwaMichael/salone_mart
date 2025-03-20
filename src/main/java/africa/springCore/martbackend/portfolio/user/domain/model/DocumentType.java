package africa.springCore.martbackend.portfolio.user.domain.model;

import africa.springCore.martbackend.core.base.domain.model.BaseEntity;
import africa.springCore.martbackend.core.base.domain.model.BioData;
import africa.springCore.martbackend.portfolio.product.domain.model.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_type")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class DocumentType extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @PrePersist
    protected void onCreate() {
        this.setCreatedAt(LocalDateTime.now());
        this.setLastModifiedAt(LocalDateTime.now());
    }

    @PreUpdate
    protected void onUpdate() {
        this.setLastModifiedAt(LocalDateTime.now());
    }
}


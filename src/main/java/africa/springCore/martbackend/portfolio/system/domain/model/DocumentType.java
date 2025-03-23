package africa.springCore.martbackend.portfolio.system.domain.model;

import africa.springCore.martbackend.core.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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


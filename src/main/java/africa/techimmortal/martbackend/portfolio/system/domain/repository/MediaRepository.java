package africa.techimmortal.martbackend.portfolio.system.domain.repository;

import africa.techimmortal.martbackend.portfolio.system.domain.model.Media;
import africa.techimmortal.martbackend.portfolio.system.domain.model.MediaCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {

    Page<Media> findAllByOwnerIdAndDocumentType(Long ownerId, String documentType, Pageable pageable);

    Page<Media> findAllByOwnerId(Long ownerId, Pageable pageable);

    Page<Media> findAllByDocumentType(String documentType, Pageable pageable);

    Media findByDocumentTypeAndOwnerId(String documentType, Long ownerId);

    Page<Media> findAllByProductIdAndTypeAndDocumentType(Long productId, MediaCategory type, String documentType, Pageable pageable);
}

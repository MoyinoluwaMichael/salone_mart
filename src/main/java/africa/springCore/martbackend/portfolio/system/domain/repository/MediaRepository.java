package africa.springCore.martbackend.portfolio.system.domain.repository;

import africa.springCore.martbackend.portfolio.system.domain.model.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {

    Page<Media> findAllByOwnerIdAndDocumentType(Long ownerId, String documentType, Pageable pageable);

    Page<Media> findAllByOwnerId(Long ownerId, Pageable pageable);

    Page<Media> findAllByDocumentType(String documentType, Pageable pageable);
}

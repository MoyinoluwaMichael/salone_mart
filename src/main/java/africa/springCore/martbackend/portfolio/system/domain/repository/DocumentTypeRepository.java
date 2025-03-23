package africa.springCore.martbackend.portfolio.system.domain.repository;

import africa.springCore.martbackend.portfolio.system.domain.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
    boolean existsByName(String name);
}

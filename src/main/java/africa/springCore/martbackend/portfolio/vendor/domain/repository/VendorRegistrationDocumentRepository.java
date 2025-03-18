package africa.springCore.martbackend.core.portfolio.vendor.domain.repository;

import africa.springCore.martbackend.core.portfolio.vendor.domain.model.VendorRegistrationDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRegistrationDocumentRepository extends JpaRepository<VendorRegistrationDocument, Long> {

    Page<VendorRegistrationDocument> findByVendorId(Long vendorId, Pageable pageable);
}

package africa.techimmortal.martbackend.portfolio.vendor.domain.repository;

import africa.techimmortal.martbackend.portfolio.vendor.domain.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    Optional<Vendor> findByBioData_EmailAddress(String emailAddress);

    Optional<Vendor> findByBioData_PhoneNumber(String phoneNumber);

    boolean existsByBusinessName(String businessName);
}

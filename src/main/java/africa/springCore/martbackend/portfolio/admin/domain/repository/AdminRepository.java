package africa.springCore.martbackend.portfolio.admin.domain.repository;

import africa.springCore.martbackend.portfolio.admin.domain.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByBioData_EmailAddress(String emailAddress);
    Optional<Admin> findByBioData_PhoneNumber(String emailAddress);
}

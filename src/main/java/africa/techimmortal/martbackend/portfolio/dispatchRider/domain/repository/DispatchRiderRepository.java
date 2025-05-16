package africa.techimmortal.martbackend.portfolio.dispatchRider.domain.repository;

import africa.techimmortal.martbackend.portfolio.dispatchRider.domain.model.DispatchRider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DispatchRiderRepository extends JpaRepository<DispatchRider, Long> {

    Optional<DispatchRider> findByBioData_EmailAddress(String emailAddress);
}

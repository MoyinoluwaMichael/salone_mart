package africa.techimmortal.martbackend.core.domain.repository;

import africa.techimmortal.martbackend.core.domain.model.BioData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BioDataRepository extends JpaRepository<BioData, Long> {

    Optional<BioData> findByEmailAddress(String email);
    Optional<BioData> findByPhoneNumber(String email);
}

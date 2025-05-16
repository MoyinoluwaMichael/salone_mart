package africa.techimmortal.martbackend.portfolio.system.domain.repository;

import africa.techimmortal.martbackend.portfolio.system.domain.model.Code;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> {

    Optional<Code> findByName(@NotBlank String name);
}

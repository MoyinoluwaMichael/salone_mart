package africa.springCore.martbackend.portfolio.system.domain.repository;

import africa.springCore.martbackend.portfolio.system.domain.model.CodeValue;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeValueRepository extends JpaRepository<CodeValue, Long> {

    Page<CodeValue> findAllByCodeId(Long id, Pageable pageable);

    boolean existsByNameAndCodeId(@NotBlank String name, Long codeId);

    Optional<CodeValue> findByCodeIdAndName(Long codeId, @NotBlank String name);
}

package africa.springCore.martbackend.core.base.domain.repository;

import africa.springCore.martbackend.common.enums.Role;
import africa.springCore.martbackend.core.base.domain.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByIdAndUserType(Long id, Role userType);
    Page<Address> findByUserIdAndUserType(Long userId, Role userType, Pageable pageable);
}

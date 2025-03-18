package africa.springCore.martbackend.core.portfolio.customer.domain.repository;

import africa.springCore.martbackend.core.portfolio.customer.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByBioData_EmailAddress(String email);

    Optional<Customer> findByBioData_PhoneNumber(String emailAddress);
}

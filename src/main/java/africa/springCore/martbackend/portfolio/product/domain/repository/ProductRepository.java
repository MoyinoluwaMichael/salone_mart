package africa.springCore.martbackend.portfolio.product.domain.repository;

import africa.springCore.martbackend.core.portfolio.product.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByVendorId(Long vendorId, Pageable pageable);

    Optional<Product> findByVendorIdAndNameAndCategoryId(Long vendorId, String name, Long id);
}

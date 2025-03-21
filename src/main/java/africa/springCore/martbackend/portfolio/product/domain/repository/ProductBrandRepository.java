package africa.springCore.martbackend.portfolio.product.domain.repository;

import africa.springCore.martbackend.portfolio.product.domain.model.ProductBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductBrandRepository extends JpaRepository<ProductBrand, Long> {

    Optional<ProductBrand> findByName(String name);
}

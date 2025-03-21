package africa.springCore.martbackend.portfolio.product.domain.repository;

import africa.springCore.martbackend.portfolio.product.domain.model.Product;
import africa.springCore.martbackend.portfolio.product.domain.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByVendorId(Long vendorId, Pageable pageable);

    Optional<Product> findByVendorIdAndNameAndCategory_IdAndBrand_Id(Long vendorId, String name, Long category_id, Long brand_id);

    boolean existsByNameAndVendorId(String hoodie, Long vendorId);
}

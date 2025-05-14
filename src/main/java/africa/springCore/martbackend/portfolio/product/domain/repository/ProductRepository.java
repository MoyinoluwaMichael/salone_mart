package africa.springCore.martbackend.portfolio.product.domain.repository;

import africa.springCore.martbackend.portfolio.product.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByVendorId(Long vendorId, Pageable pageable);

    Optional<Product> findByVendorIdAndNameAndCategory(Long vendorId, String name, String category);

    boolean existsByNameAndVendorId(String hoodie, Long vendorId);

    @Query(value = "SELECT category_id, COUNT(id) FROM product GROUP BY category_id ORDER BY COUNT(id) DESC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<Object[]> findTopByProductCount(@Param("limit") int limit, @Param("offset") long offset);


}

package africa.springCore.martbackend.infrastructure.configuration.datainitializer.product;

import africa.springCore.martbackend.portfolio.product.domain.model.ProductCategory;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductCategoryInitializer {

    private final ProductCategoryRepository productCategoryRepository;

    @PostConstruct
    public void init() {
        log.info("Initializing default product categories...");
        Map<String, String> categoryMap = new HashMap<>();
        categoryMap.put("FASHION", "Fashion");
        categoryMap.put("ELECTRONICS", "Electronics");
        categoryMap.put("HOME_AND_OFFICE", "Home & Office");
        categoryMap.put("PHONES_AND_TABLETS", "Phones & Tablets");
        categoryMap.put("COMPUTING", "Computing");
        categoryMap.put("SUPERMARKET", "Supermarket");

        long idCounter = 1;
        for (Map.Entry<String, String> entry : categoryMap.entrySet()) {
            if (productCategoryRepository.findByName(entry.getKey()).isEmpty()) {
                ProductCategory category = new ProductCategory();
                category.setId(idCounter++);
                category.setName(entry.getKey());
                category.setDescription(entry.getValue());
                productCategoryRepository.save(category);
            }
        }
    }
}

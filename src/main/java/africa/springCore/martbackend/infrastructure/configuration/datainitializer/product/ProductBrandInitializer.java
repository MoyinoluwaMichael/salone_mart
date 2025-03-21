package africa.springCore.martbackend.infrastructure.configuration.datainitializer.product;

import africa.springCore.martbackend.portfolio.product.domain.model.ProductBrand;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductBrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductBrandInitializer {

    private final ProductBrandRepository productBrandRepository;

    @PostConstruct
    public void init() {
        log.info("Initializing default product brands...");
        Map<String, String> brandMap = new HashMap<>();
        brandMap.put("Adidas", "Adidas Brand");
        brandMap.put("Nike", "Nike Brand");
        brandMap.put("Puma", "Puma Brand");
        brandMap.put("Reebok", "Reebok Brand");
        brandMap.put("Asics", "Asics Brand");
        brandMap.put("Vans", "Vans Brand");
        brandMap.put("Fila", "Fila Brand");

        long idCounter = 1;
        for (Map.Entry<String, String> entry : brandMap.entrySet()) {
            String brandName = entry.getKey().toUpperCase();
            if (productBrandRepository.findByName(brandName).isEmpty()) {
                ProductBrand brand = new ProductBrand();
                brand.setId(idCounter++);
                brand.setName(brandName);
                brand.setDescription(entry.getValue());
                productBrandRepository.save(brand);
            }
        }
    }
}

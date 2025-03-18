package africa.springCore.martbackend.infrastructure.configuration;

import africa.springCore.martbackend.portfolio.product.domain.model.ProductCategory;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
public class DataInitializer {


    @Bean
    @Transactional
    public CommandLineRunner loadData(ProductCategoryRepository productCategoryRepository) {
        return args -> {
            List<String> categories = List.of(
                    "FASHION",
                    "ELECTRONICS",
                    "HOME_AND_OFFICE",
                    "PHONES_AND_TABLETS",
                    "COMPUTING",
                    "SUPERMARKET"
            );

            for (String category : categories) {
                if (productCategoryRepository.findByName(category).isEmpty()) {
                    productCategoryRepository.save(ProductCategory.instance(category));
                }
            }
        };
    }
}

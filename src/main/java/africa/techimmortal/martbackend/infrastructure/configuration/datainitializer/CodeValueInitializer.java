package africa.techimmortal.martbackend.infrastructure.configuration.datainitializer;

import africa.techimmortal.martbackend.portfolio.system.domain.model.Code;
import africa.techimmortal.martbackend.portfolio.system.domain.model.CodeValue;
import africa.techimmortal.martbackend.portfolio.system.domain.repository.CodeRepository;
import africa.techimmortal.martbackend.portfolio.system.domain.repository.CodeValueRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CodeValueInitializer {

    @Bean
    CommandLineRunner codes(CodeRepository codeRepository, CodeValueRepository codeValueRepository) {
        return args -> {
            Code categoryCode = findOrCreateCode("Product Category", codeRepository);
            Code vendorDocCode = findOrCreateCode("Vendor Documentation Requirement", codeRepository);
            Code documentType = findOrCreateCode("Document Type", codeRepository);

            saveCodeValueIfNotExists("Display Picture", documentType, codeValueRepository);

            saveCodeValueIfNotExists("Electronics", categoryCode, codeValueRepository);
            saveCodeValueIfNotExists("Clothing", categoryCode, codeValueRepository);
            saveCodeValueIfNotExists("Home & Office", categoryCode, codeValueRepository);
            saveCodeValueIfNotExists("Phones & Tablets", categoryCode, codeValueRepository);
            saveCodeValueIfNotExists("Computing", categoryCode, codeValueRepository);
            saveCodeValueIfNotExists("Supermarket", categoryCode, codeValueRepository);
        };
    }

    private Code findOrCreateCode(String name, CodeRepository repository) {
        return repository.findByName(name).orElseGet(() -> repository.save(new Code(name, true)));
    }

    private void saveCodeValueIfNotExists(String value, Code code, CodeValueRepository repository) {
        boolean exists = repository.existsByNameAndCodeId(value, code.getId());
        if (!exists) {
            repository.save(new CodeValue(value, code));
        }
    }


}

package africa.springCore.martbackend.infrastructure.configuration.datainitializer;

import africa.springCore.martbackend.portfolio.user.domain.model.DocumentType;
import africa.springCore.martbackend.portfolio.user.domain.repository.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DocumentTypeInitializer {

    private final DocumentTypeRepository documentTypeRepository;

    @PostConstruct
    public void init() {
        List<DocumentType> documentTypes = Arrays.asList(
                new DocumentType(1L, "DISPLAY_PICTURE", "Profile display picture"),
                new DocumentType(2L, "BUSINESS_CERTIFICATE", "Business certification document"),
                new DocumentType(3L, "ID_CARD", "Identification card")
        );

        for (DocumentType documentType : documentTypes) {
            if (!documentTypeRepository.existsByName(documentType.getName())) {
                documentTypeRepository.save(documentType);
            }
        }
    }
}

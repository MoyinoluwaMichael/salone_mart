package africa.techimmortal.martbackend.infrastructure.configuration.datainitializer.vendor;

import africa.techimmortal.martbackend.core.utils.MartMapper;
import africa.techimmortal.martbackend.portfolio.system.domain.dto.FileMetaData;
import africa.techimmortal.martbackend.portfolio.vendor.domain.dtos.requests.VendorCreationRequest;
import africa.techimmortal.martbackend.portfolio.vendor.domain.dtos.responses.VendorResponseDto;
import africa.techimmortal.martbackend.portfolio.system.service.MediaService;
import africa.techimmortal.martbackend.portfolio.vendor.domain.repository.VendorRepository;
import africa.techimmortal.martbackend.portfolio.vendor.service.VendorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@DependsOn("codeValueInitializer")
public class VendorInitializer {

    private final VendorService vendorService;
    private final VendorRepository vendorRepository;
    private final MediaService mediaService;
    private final MartMapper martMapper;

    @PostConstruct
    public void init() {
        log.info("Initializing default Vendors...");
        try {
            if (vendorRepository.findByBioData_EmailAddress("moyinoluwamichaelz@gmail.com").isPresent()) {
                return;
            }

            VendorCreationRequest vendorCreationRequest = new VendorCreationRequest();
            vendorCreationRequest.setPassword("password");
            vendorCreationRequest.setEmailAddress("moyinoluwamichaelz@gmail.com");
            vendorCreationRequest.setFirstName("Mary-Ann");
            vendorCreationRequest.setLastName("Kai Kai");
            vendorCreationRequest.setBusinessName("Vendor Business Name");

            VendorResponseDto vendorResponseDto = vendorService.createVendor(vendorCreationRequest);

            List<MultipartFile> multipartFiles = new ArrayList<>();

            File imageFile = new File("src/main/java/africa/techimmortal/martbackend/infrastructure/configuration/datainitializer/vendor/image/vendor0001.jpg");
            File pdfFile = new File("src/main/java/africa/techimmortal/martbackend/infrastructure/configuration/datainitializer/vendor/document/vendor0001_bus_cert.pdf");

            if (!imageFile.exists() || !pdfFile.exists()) {
                throw new IOException("Required files not found");
            }

            byte[] imageContent = Files.readAllBytes(imageFile.toPath());
            MultipartFile imageMultipartFile = new MockMultipartFile(
                    "file",
                    imageFile.getName(),
                    MediaType.IMAGE_JPEG_VALUE,
                    imageContent
            );

            byte[] pdfContent = Files.readAllBytes(pdfFile.toPath());
            MultipartFile pdfMultipartFile = new MockMultipartFile(
                    "file",
                    pdfFile.getName(),
                    MediaType.APPLICATION_PDF_VALUE,
                    pdfContent
            );

            multipartFiles.add(imageMultipartFile);
            multipartFiles.add(pdfMultipartFile);

            List<FileMetaData> metaDataList = new ArrayList<>();
            FileMetaData metaData = new FileMetaData();
            metaData.setMediaCategory("USER");
            metaData.setId(imageMultipartFile.getOriginalFilename());
            metaData.setDocumentType("Display Picture");
            metaDataList.add(metaData);

            FileMetaData metaData2 = new FileMetaData();
            metaData2.setMediaCategory("DOCUMENT");
            metaData2.setId(pdfMultipartFile.getOriginalFilename());
            metaData2.setDocumentType("Business Certificate");
            metaDataList.add(metaData2);

            mediaService.uploadProfilePicture(multipartFiles.get(0), vendorResponseDto.getBioData().getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package africa.springCore.martbackend.infrastructure.configuration.datainitializer.product;

import africa.springCore.martbackend.core.domain.enums.ApprovalStatus;
import africa.springCore.martbackend.core.utils.MartMapper;
import africa.springCore.martbackend.portfolio.product.domain.dtos.request.ProductCreationRequest;
import africa.springCore.martbackend.portfolio.product.domain.model.Product;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductRepository;
import africa.springCore.martbackend.portfolio.product.service.ProductService;
import africa.springCore.martbackend.portfolio.system.domain.dto.FileMetaData;
import africa.springCore.martbackend.portfolio.system.domain.model.MediaCategory;
import africa.springCore.martbackend.portfolio.system.service.MediaService;
import africa.springCore.martbackend.portfolio.vendor.domain.dtos.responses.VendorResponseDto;
import africa.springCore.martbackend.portfolio.vendor.domain.model.Vendor;
import africa.springCore.martbackend.portfolio.vendor.domain.repository.VendorRepository;
import africa.springCore.martbackend.portfolio.vendor.service.VendorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static africa.springCore.martbackend.portfolio.system.service.MediaServiceImpl.DISPLAY_PICTURE;

@Configuration
@RequiredArgsConstructor
@Slf4j
@DependsOn({"codeValueInitializer", "vendorInitializer"})
public class ProductInitializer {

    private final ProductService productService;
    private final VendorService vendorService;
    private final VendorRepository vendorRepository;
    private final MediaService mediaService;
    private final MartMapper martMapper;
    private final ProductRepository productRepository;

    private static final String BASE_IMAGE_PATH = "src/main/java/africa/springCore/martbackend/infrastructure/configuration/datainitializer/product/image/";
    private static final String VENDOR_EMAIL = "moyinoluwamichaelz@gmail.com";

    @PostConstruct
    public void init() {
        log.info("Initializing default products...");
        try {
            VendorResponseDto vendorResponseDto = vendorService.findByEmail(VENDOR_EMAIL);
            Long vendorId = vendorResponseDto.getId();
            Long bioDataId = vendorResponseDto.getBioData().getId();

            if (productRepository.existsByNameAndVendorId("Hoodie", vendorId)) {
                log.info("Products already initialized for vendor ID: {}", vendorId);
                return;
            }

            Vendor vendor = vendorRepository.findByBioData_EmailAddress(VENDOR_EMAIL).get();
            vendor.setStatus(ApprovalStatus.APPROVED);
            vendorRepository.save(vendor);

            Map<String, Object[]> products = Map.of(
                    "Hoodie", new Object[]{"Athletic Performance Hoodie.", "FASHION", "Adidas", "Hoodies", 199.99, 20L, null, "AthleticPerformancHoodie.jpeg"},
                    "Men Jacket", new Object[]{"Black Leather Jacket.", "FASHION", "Adidas", "jacket", 434.0, 25L, null, "BlackLeatherJacket.jpeg"},
                    "Denim Jeans", new Object[]{"Blue Denim Jeans.", "FASHION", "Adidas", "Jeans", 32.0, 20L, null, "BlueDenimJeans.jpeg"},
                    "Plaid Shirt", new Object[]{"Casual Plaid Shirt.", "FASHION", "Adidas", "Shirt", 54.0, 60L, null, "CasualPlaidShirt.jpeg"},
                    "Floral Dress", new Object[]{"Summer Floral Dress.", "FASHION", "Adidas", "Dress", 354.0, 40L, 10L, "SummerFloralDress.png"},
                    "T Shirt", new Object[]{"White T Shirt.", "FASHION", "Adidas", "T-shirt", 35.0, 100L, 5L, "whiteT_shirt.jpeg"}
            );

            Long brandId = 1L;
            for (var entry : products.entrySet()) {
                List<MultipartFile> multipartFiles = new ArrayList<>();
                List<FileMetaData> metaDataList = new ArrayList<>();

                String name = entry.getKey();
                Object[] data = entry.getValue();

                // Create product
                ProductCreationRequest request = createProductRequest(
                        name, (String)data[0], brandId,
                        BigDecimal.valueOf((Double)data[4]),
                        (Long)data[5], (Long)data[6]
                );
                brandId++;

                request.setVendorId(vendorId);
                Product response = productService.postAProduct(request);

                // Prepare image
                String imagePath = BASE_IMAGE_PATH + data[7];
                File imageFile = new File(imagePath);
                if (!imageFile.exists()) {
                    throw new IOException("Image file not found: " + imagePath);
                }

                byte[] imageContent = Files.readAllBytes(imageFile.toPath());
                MultipartFile imageMultipartFile = new MockMultipartFile(
                        "file", imageFile.getName(),
                        MediaType.IMAGE_JPEG_VALUE, imageContent
                );
                multipartFiles.add(imageMultipartFile);

                // Add metadata
                FileMetaData metaData = new FileMetaData();
                metaData.setMediaCategory(MediaCategory.PRODUCT.name());
                metaData.setId(imageFile.getName());
                metaData.setDocumentType(DISPLAY_PICTURE);
                metaDataList.add(metaData);

                // Upload media
                mediaService.uploadProductMedia(
                        multipartFiles,
                        martMapper.writeValueAsString(metaDataList),
                        response.getId(),
                        bioDataId);
            }

            log.info("Products initialized successfully");
        } catch (Exception e) {
            log.error("Error initializing products", e);
        }
    }

    private ProductCreationRequest createProductRequest(String name, String description,
                                                        Long brandId,
                                                        BigDecimal price,
                                                        Long quantity, Long interest) {
        ProductCreationRequest request = new ProductCreationRequest();
        request.setName(name);
        request.setDescription(description);
        request.setCategory("Clothing");
        request.setPrice(price);
        request.setQuantity(quantity);
        request.setDiscount(interest);
        return request;
    }
}

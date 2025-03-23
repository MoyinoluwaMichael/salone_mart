package africa.springCore.martbackend.portfolio.system.service;

import africa.springCore.martbackend.core.domain.enums.Role;
import africa.springCore.martbackend.core.utils.MartMapper;
import africa.springCore.martbackend.core.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.core.domain.model.BioData;
import africa.springCore.martbackend.core.domain.repository.BioDataRepository;
import africa.springCore.martbackend.infrastructure.cloudservice.storageservice.service.CloudinaryUploadService;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.MediaUploadFailedException;
import africa.springCore.martbackend.portfolio.product.domain.model.Product;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductRepository;
import africa.springCore.martbackend.portfolio.system.domain.dto.FileMetaData;
import africa.springCore.martbackend.portfolio.system.domain.model.DocumentType;
import africa.springCore.martbackend.portfolio.system.domain.model.Media;
import africa.springCore.martbackend.portfolio.system.domain.model.MediaCategory;
import africa.springCore.martbackend.portfolio.system.domain.repository.DocumentTypeRepository;
import africa.springCore.martbackend.portfolio.vendor.domain.model.Vendor;
import africa.springCore.martbackend.portfolio.vendor.domain.repository.VendorRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static africa.springCore.martbackend.core.utils.AppUtils.DOCUMENT_FOLDER;
import static africa.springCore.martbackend.core.utils.AppUtils.PRODUCT_FOLDER;
import static africa.springCore.martbackend.core.utils.AppUtils.USER_PROFILE_FOLDER;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final MartMapper martMapper;
    private final BioDataRepository bioDataRepository;
    private final VendorRepository vendorRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final CloudinaryUploadService cloudinaryUploadService;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public BioDataResponseDto uploadMedia(List<MultipartFile> files, String fileMetaData, Long userId, Long productId) throws MapperException, MediaUploadFailedException {
        TypeReference<List<FileMetaData>> typeReference = new TypeReference<>() {
        };
        List<FileMetaData> metaData = martMapper.readValue(fileMetaData, typeReference);
        Product product = null;
        if (productId != null) {
            product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        }
        BioData existingBioData = bioDataRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Vendor vendor = null;
        if (existingBioData.getRoles().contains(Role.VENDOR)) {
            vendor = vendorRepository.findByBioData_EmailAddress(existingBioData.getEmailAddress()).get();
        }
        if (!files.isEmpty()) {
            for (MultipartFile file : files) {
                String fileId = file.getOriginalFilename();
                FileMetaData metadata = metaData.stream()
                        .filter(meta -> meta.getId().equals(fileId))
                        .findFirst()
                        .orElse(null);
                if (metadata != null) {
                    MediaCategory requestMediaCategory = MediaCategory.instanceOf(metadata.getMediaCategory());
                    Media media = existingBioData.getMediaByDocumentTypeId(metadata.getDocumentTypeId());
                    if (media != null && media.getType().equals(requestMediaCategory)) {
                        try {
                            BioData savedBioData = existingBioData.removeMedia(media, cloudinaryUploadService);
                            bioDataRepository.save(savedBioData);
                        } catch (MediaUploadFailedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    DocumentType documentType = documentTypeRepository.findById(metadata.getDocumentTypeId())
                            .orElseThrow(() -> new MediaUploadFailedException("Document type not found"));
                    uploadAndAddMedia(existingBioData, file, product, documentType, requestMediaCategory, vendor);
                }
            }
            if (product != null) {
                productRepository.save(product);
            }
            bioDataRepository.save(existingBioData);
        }

        return martMapper.readValue(existingBioData, BioDataResponseDto.class);
    }

    public void uploadAndAddMedia(BioData bioData, MultipartFile file, Product product, DocumentType documentType, MediaCategory requestMediaCategory, Vendor vendor) throws MediaUploadFailedException {
        String contentType = file.getContentType();
        if ((contentType != null && contentType.startsWith("image/")) && MediaCategory.USER.equals(requestMediaCategory)) {
            bioData.uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.USER, USER_PROFILE_FOLDER, documentType);
        } else if (MediaCategory.DOCUMENT.equals(requestMediaCategory)) {
            if (vendor != null) {
                vendor.uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.DOCUMENT, DOCUMENT_FOLDER, documentType);
            }else {
                bioData.uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.PRODUCT, PRODUCT_FOLDER, documentType);
            }
        } else if (MediaCategory.PRODUCT.equals(requestMediaCategory) && product != null) {
            product.uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.PRODUCT, PRODUCT_FOLDER, documentType);
        }
    }
}

package africa.springCore.martbackend.portfolio.system.service;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
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
import africa.springCore.martbackend.portfolio.system.domain.model.Media;
import africa.springCore.martbackend.portfolio.system.domain.model.MediaCategory;
import africa.springCore.martbackend.portfolio.system.domain.repository.MediaRepository;
import africa.springCore.martbackend.portfolio.vendor.domain.model.Vendor;
import africa.springCore.martbackend.portfolio.vendor.domain.repository.VendorRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static africa.springCore.martbackend.core.utils.AppUtils.DOCUMENT_FOLDER;
import static africa.springCore.martbackend.core.utils.AppUtils.PRODUCT_FOLDER;
import static africa.springCore.martbackend.core.utils.AppUtils.USER_PROFILE_FOLDER;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaServiceImpl implements MediaService {
    private final MartMapper martMapper;
    private final BioDataRepository bioDataRepository;
    private final VendorRepository vendorRepository;
    private final CloudinaryUploadService cloudinaryUploadService;
    private final ProductRepository productRepository;
    private final MediaRepository mediaRepository;

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
                    Media media = existingBioData.getMediaByDocumentType(metadata.getDocumentType());
                    if (media != null && media.getType().equals(requestMediaCategory)) {
                        try {
                            BioData savedBioData = existingBioData.removeMedia(media, cloudinaryUploadService);
                            bioDataRepository.save(savedBioData);
                        } catch (MediaUploadFailedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    uploadAndAddMedia(existingBioData, file, product, metadata.getDocumentType(), requestMediaCategory, vendor);
                    log.info("File uploaded: {}", file.getOriginalFilename());
                }
            }
            if (product != null) {
                productRepository.save(product);
            }
            bioDataRepository.save(existingBioData);
        }

        return martMapper.readValue(existingBioData, BioDataResponseDto.class);
    }

    @Override
    @Transactional
    public String uploadProductMedia(List<MultipartFile> files, String fileMetaData, Long productId) throws MapperException, MediaUploadFailedException {
        TypeReference<List<FileMetaData>> typeReference = new TypeReference<>() {
        };
        List<FileMetaData> metaData = martMapper.readValue(fileMetaData, typeReference);
        Product product;
        if (productId != null) {
            product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
            if (!files.isEmpty()) {
                for (MultipartFile file : files) {
                    String fileId = file.getOriginalFilename();
                    FileMetaData metadata = metaData.stream()
                            .filter(meta -> meta.getId().equals(fileId))
                            .findFirst()
                            .orElse(null);
                    if (metadata != null) {
                        uploadAndAddMedia(null, file, product, metadata.getDocumentType(), MediaCategory.PRODUCT, null);
                        log.info("File uploaded: {}", file.getOriginalFilename());
                    }
                }
                productRepository.save(product);
            }

            return "File uploaded successfully";
        }
        throw new RuntimeException("Product not found");
    }

    @Override
    public BasePageableResponse<Media> retrieveAllMedia(Long ownerId, String documentType, Pageable pageable) {
        BasePageableResponse<Media> mediaBasePageableResponse;
        if (ownerId != null && StringUtils.isNotBlank(documentType)) {
            mediaBasePageableResponse = BasePageableResponse.instance(mediaRepository.findAllByOwnerIdAndDocumentType(ownerId, documentType, pageable));
        } else if (ownerId != null) {
            mediaBasePageableResponse = BasePageableResponse.instance(mediaRepository.findAllByOwnerId(ownerId, pageable));
        } else if (StringUtils.isNotBlank(documentType)) {
            mediaBasePageableResponse = BasePageableResponse.instance(mediaRepository.findAllByDocumentType(documentType, pageable));
        } else {
            mediaBasePageableResponse = BasePageableResponse.instance(mediaRepository.findAll(pageable));
        }
        return mediaBasePageableResponse;
    }

    public void uploadAndAddMedia(BioData bioData, MultipartFile file, Product product, String documentType, MediaCategory requestMediaCategory, Vendor vendor) throws MediaUploadFailedException {
        String contentType = file.getContentType();
        Media media = null;
        if ((contentType != null && contentType.startsWith("image/")) && MediaCategory.USER.equals(requestMediaCategory)) {
            bioData.uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.USER, USER_PROFILE_FOLDER, documentType);
        } else if (MediaCategory.DOCUMENT.equals(requestMediaCategory)) {
            if (vendor != null) {
                media = vendor.uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.DOCUMENT, DOCUMENT_FOLDER, documentType);
            }else {
                bioData.uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.PRODUCT, PRODUCT_FOLDER, documentType);
            }
        } else if (MediaCategory.PRODUCT.equals(requestMediaCategory) && product != null) {
            media = product.uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.PRODUCT, PRODUCT_FOLDER, documentType);
        }
        if (media != null) {
            mediaRepository.save(media);
        }
    }
}

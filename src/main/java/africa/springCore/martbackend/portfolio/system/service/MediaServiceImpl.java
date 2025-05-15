package africa.springCore.martbackend.portfolio.system.service;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.core.utils.MartMapper;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Media uploadProfilePicture(MultipartFile file, Long userId) throws MediaUploadFailedException, MapperException {
        BioData existingBioData = bioDataRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (!file.isEmpty()) {
            String contentType = file.getContentType();
            if ((contentType != null && contentType.startsWith("image/"))) {
                uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.USER, USER_PROFILE_FOLDER, "Display Picture", existingBioData);
            }
            log.info("File uploaded: {}", file.getOriginalFilename());
            bioDataRepository.save(existingBioData);
        }
        return existingBioData.getDisplayPicture();
    }

    public BioData uploadAndAddMedia(MultipartFile file, CloudinaryUploadService cloudinaryUploadService, MediaCategory mediaCategory, String folderName, String documentType, BioData bioData) throws MediaUploadFailedException {
        Map<String, Object> uploadResponse = new HashMap<>();
        try {
            uploadResponse = cloudinaryUploadService.uploadFile(file, folderName);
        } catch (Exception e) {
            throw new MediaUploadFailedException("User image upload failed: "+ e.getMessage());
        }
        if (uploadResponse.containsKey("error")) {
            throw new MediaUploadFailedException("User image upload failed");
        }

        String publicId = (String) uploadResponse.get("public_id");
        String secureUrl = (String) uploadResponse.get("secure_url");
        Media media = Media.userInstance(mediaCategory, documentType, publicId, secureUrl, file, bioData.getId());
        if (bioData.getDisplayPicture() != null) {
            this.removeMedia(media);
        }
        bioData.setDisplayPicture(media);
        return bioData;
    }

    public void removeMedia(Media media) throws MediaUploadFailedException {
        try {
            this.cloudinaryUploadService.deleteFile(media.getPublicId());
        } catch (Exception e) {
            throw new MediaUploadFailedException(e.getMessage());
        }
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
            uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.USER, USER_PROFILE_FOLDER, documentType, bioData);
        } else if (MediaCategory.DOCUMENT.equals(requestMediaCategory)) {
            if (vendor != null) {
                media = vendor.uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.DOCUMENT, DOCUMENT_FOLDER, documentType);
            } else {
                uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.PRODUCT, PRODUCT_FOLDER, documentType, bioData);
            }
        } else if (MediaCategory.PRODUCT.equals(requestMediaCategory) && product != null) {
            media = product.uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.PRODUCT, PRODUCT_FOLDER, documentType);
        }
        if (media != null) {
            mediaRepository.save(media);
        }
    }
}

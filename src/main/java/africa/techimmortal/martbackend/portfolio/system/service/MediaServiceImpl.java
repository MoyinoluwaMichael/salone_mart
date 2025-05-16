package africa.techimmortal.martbackend.portfolio.system.service;

import africa.techimmortal.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.techimmortal.martbackend.core.utils.MartMapper;
import africa.techimmortal.martbackend.core.domain.model.BioData;
import africa.techimmortal.martbackend.core.domain.repository.BioDataRepository;
import africa.techimmortal.martbackend.infrastructure.cloudservice.storageservice.service.CloudinaryUploadService;
import africa.techimmortal.martbackend.infrastructure.exception.EntityValidationException;
import africa.techimmortal.martbackend.infrastructure.exception.MapperException;
import africa.techimmortal.martbackend.infrastructure.exception.MediaUploadFailedException;
import africa.techimmortal.martbackend.portfolio.product.domain.model.Product;
import africa.techimmortal.martbackend.portfolio.product.domain.repository.ProductRepository;
import africa.techimmortal.martbackend.portfolio.system.domain.dto.FileMetaData;
import africa.techimmortal.martbackend.portfolio.system.domain.model.Media;
import africa.techimmortal.martbackend.portfolio.system.domain.model.MediaCategory;
import africa.techimmortal.martbackend.portfolio.system.domain.repository.MediaRepository;
import africa.techimmortal.martbackend.portfolio.vendor.domain.repository.VendorRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static africa.techimmortal.martbackend.core.utils.AppUtils.PRODUCT_FOLDER;
import static africa.techimmortal.martbackend.core.utils.AppUtils.USER_PROFILE_FOLDER;

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

    public static final String DISPLAY_PICTURE = "Display Picture";

    @Override
    @Transactional
    public String uploadProductMedia(List<MultipartFile> files, String fileMetaData, Long productId, Long bioDataId) throws MapperException, MediaUploadFailedException {
        TypeReference<List<FileMetaData>> typeReference = new TypeReference<>() {
        };
        List<FileMetaData> metaData = martMapper.readValue(fileMetaData, typeReference);
        Product product;
        if (productId != null) {
            product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
            if (!files.isEmpty()) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        String fileId = file.getOriginalFilename();
                        FileMetaData metadata = metaData.stream()
                                .filter(meta -> meta.getId().equals(fileId))
                                .findFirst()
                                .orElse(null);
                        if (metadata != null) {
                            uploadAndAddProductDisplayPicture(file, bioDataId, productId);
                            log.info("File uploaded: {}", file.getOriginalFilename());
                        }
                    }
                }
                productRepository.save(product);
            }

            return "File uploaded successfully";
        }
        throw new RuntimeException("Product not found");
    }

    @Override
    public Media uploadProfilePicture(MultipartFile file, Long userId) throws MediaUploadFailedException {
        BioData existingBioData = bioDataRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (!file.isEmpty()) {
            String contentType = file.getContentType();
            if ((contentType != null && contentType.startsWith("image/"))) {
                uploadAndAddUserDisplayPicture(file, existingBioData);
            }
            log.info("File uploaded: {}", file.getOriginalFilename());
            bioDataRepository.save(existingBioData);
        }
        return existingBioData.getDisplayPicture();
    }

    @Override
    public BasePageableResponse<Media> retrieveProductMedia(Long productId, Pageable pageable) throws EntityValidationException {
        productRepository.findById(productId).orElseThrow(() -> new EntityValidationException("Product with ID "+productId+" not found"));
        Page<Media> mediaPage = mediaRepository.findAllByProductIdAndTypeAndDocumentType(productId, MediaCategory.PRODUCT, DISPLAY_PICTURE, pageable);
        return BasePageableResponse.instance(mediaPage);
    }

    public BioData uploadAndAddUserDisplayPicture(MultipartFile file, BioData bioData) throws MediaUploadFailedException {
        Map<String, Object> uploadResponse = this.uploadToCloudinary(file, USER_PROFILE_FOLDER);
        String publicId = (String) uploadResponse.get("public_id");
        String secureUrl = (String) uploadResponse.get("secure_url");
        Media media = Media.userInstance(MediaCategory.USER, DISPLAY_PICTURE, publicId, secureUrl, file, bioData.getId());
        if (bioData.getDisplayPicture() != null) {
            this.removeMedia(media);
        }
        bioData.setDisplayPicture(media);
        return bioData;
    }

    private Map<String, Object> uploadToCloudinary(MultipartFile file, String folderName) throws MediaUploadFailedException {
        Map<String, Object> uploadResponse = new HashMap<>();
        try {
            uploadResponse = this.cloudinaryUploadService.uploadFile(file, folderName);
        } catch (Exception e) {
            throw new MediaUploadFailedException("User image upload failed: "+ e.getMessage());
        }
        if (uploadResponse.containsKey("error")) {
            throw new MediaUploadFailedException("User image upload failed");
        }
        return uploadResponse;
    }


    public Media uploadAndAddProductDisplayPicture(MultipartFile file, Long ownerId, Long productId) throws MediaUploadFailedException {
        Map<String, Object> uploadResponse = this.uploadToCloudinary(file, PRODUCT_FOLDER);

        String publicId = (String) uploadResponse.get("public_id");
        String secureUrl = (String) uploadResponse.get("secure_url");
        Media media = Media.userInstance(MediaCategory.PRODUCT, DISPLAY_PICTURE, publicId, secureUrl, file, ownerId, productId);
        mediaRepository.save(media);
        return media;
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

}

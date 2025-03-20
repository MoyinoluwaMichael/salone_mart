package africa.springCore.martbackend.portfolio.user.service;

import africa.springCore.martbackend.common.utils.MartMapper;
import africa.springCore.martbackend.core.base.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.core.base.domain.model.BioData;
import africa.springCore.martbackend.core.base.domain.repository.BioDataRepository;
import africa.springCore.martbackend.infrastructure.cloudservice.storageservice.service.CloudinaryUploadService;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.MediaUploadFailedException;
import africa.springCore.martbackend.portfolio.product.domain.model.Product;
import africa.springCore.martbackend.portfolio.product.domain.repository.ProductRepository;
import africa.springCore.martbackend.portfolio.user.domain.dto.FileMetaData;
import africa.springCore.martbackend.portfolio.user.domain.model.DocumentType;
import africa.springCore.martbackend.portfolio.user.domain.model.Media;
import africa.springCore.martbackend.portfolio.user.domain.model.MediaCategory;
import africa.springCore.martbackend.portfolio.user.domain.repository.DocumentTypeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static africa.springCore.martbackend.common.utils.AppUtils.*;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final MartMapper martMapper;
    private final BioDataRepository bioDataRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final CloudinaryUploadService cloudinaryUploadService;
    private final ProductRepository productRepository;

    @Override
    public BioDataResponseDto uploadMedia(List<MultipartFile> files, String fileMetaData, Long userId, Long productId) throws MapperException, MediaUploadFailedException {
        TypeReference<List<FileMetaData>> typeReference = new TypeReference<>() {
        };
        List<FileMetaData> metaData = martMapper.readValue(fileMetaData, typeReference);
        Product product = null;
        if (productId != null) {
            product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        }
        BioData existingBioData = bioDataRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (!files.isEmpty()) {
            for (MultipartFile file : files) {
                String fileId = file.getOriginalFilename();
                FileMetaData metadata = metaData.stream()
                        .filter(meta -> meta.getId().equals(fileId))
                        .findFirst()
                        .orElse(null);
                if (metadata != null) {
                    Media media = existingBioData.getDocumentTypeId(metadata.getDocumentTypeId());
                    System.err.println("media: " + media);
                    if (media != null) {
                        try {
                            BioData savedBioData = existingBioData.removeMedia(media, cloudinaryUploadService);
                            bioDataRepository.save(savedBioData);
                        } catch (MediaUploadFailedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    DocumentType documentType = documentTypeRepository.findById(metadata.getDocumentTypeId())
                            .orElseThrow(() -> new MediaUploadFailedException("Document type not found"));
                    uploadAndAddMedia(existingBioData, file, metadata, product, documentType);
                }
            }
            bioDataRepository.save(existingBioData);
        }

        return martMapper.readValue(existingBioData, BioDataResponseDto.class);
    }


    private void uploadAndAddMedia(BioData bioData, MultipartFile file, FileMetaData metadata, Product product, DocumentType documentType) throws MediaUploadFailedException {
        String contentType = file.getContentType();
        MediaCategory requestMediaCategory = MediaCategory.instanceOf(metadata.getMediaCategory());
        if ((contentType != null && contentType.startsWith("image/")) || MediaCategory.USER.equals(requestMediaCategory)) {
            bioData.uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.USER, USER_PROFILE_FOLDER, product, documentType);
        } else if (MediaCategory.DOCUMENT.equals(requestMediaCategory)) {
            bioData.uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.DOCUMENT, DOCUMENT_FOLDER, product, documentType);
        } else if (MediaCategory.PRODUCT.equals(requestMediaCategory) && product != null) {
            bioData.uploadAndAddMedia(file, cloudinaryUploadService, MediaCategory.PRODUCT, PRODUCT_FOLDER, product, documentType);
        }
    }
}

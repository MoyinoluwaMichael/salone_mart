package africa.springCore.martbackend.portfolio.system.service;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.infrastructure.exception.EntityValidationException;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.MediaUploadFailedException;
import africa.springCore.martbackend.portfolio.system.domain.model.Media;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {

    BasePageableResponse<Media> retrieveAllMedia(Long ownerId, String documentType, Pageable pageable);

    String uploadProductMedia(List<MultipartFile> files, String fileMetaData, Long productId, Long bioDataId) throws MapperException, MediaUploadFailedException;

    Media uploadProfilePicture(MultipartFile file, Long userId) throws MediaUploadFailedException, MapperException;

    BasePageableResponse<Media> retrieveProductMedia(Long productId, Pageable pageable) throws EntityValidationException;
}

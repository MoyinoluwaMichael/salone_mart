package africa.springCore.martbackend.portfolio.system.service;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.core.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.MediaUploadFailedException;
import africa.springCore.martbackend.portfolio.system.domain.model.Media;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    BioDataResponseDto uploadMedia(List<MultipartFile> files, String fileMetaData, Long userId, Long productId) throws MapperException, MediaUploadFailedException;

    BasePageableResponse<Media> retrieveAllMedia(Long ownerId, String documentType, Pageable pageable);

    String uploadProductMedia(List<MultipartFile> files, String fileMetaData, Long productId) throws MapperException, MediaUploadFailedException;
}

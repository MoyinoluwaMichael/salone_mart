package africa.springCore.martbackend.portfolio.user.service;

import africa.springCore.martbackend.core.base.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.MediaUploadFailedException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    BioDataResponseDto uploadMedia(List<MultipartFile> files, String fileMetaData, Long userId, Long productId) throws MapperException, MediaUploadFailedException;
}

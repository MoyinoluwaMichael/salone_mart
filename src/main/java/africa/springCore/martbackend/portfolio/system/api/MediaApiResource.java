package africa.springCore.martbackend.portfolio.system.api;

import africa.springCore.martbackend.core.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.MediaUploadFailedException;
import africa.springCore.martbackend.portfolio.system.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("api/v1/media")
@RestController
@RequiredArgsConstructor
public class MediaApiResource {
    private final MediaService mediaService;

    @Operation(
            summary = "Upload media files",
            description = "API for uploading media files."
    )
    @PostMapping("/upload")
    public ResponseEntity<BioDataResponseDto> uploadMedia(
            @RequestParam("file") List<MultipartFile> files,
            @RequestParam("fileMetaData") String fileMetaData,
            @RequestParam("userId") Long userId,
            @RequestParam(name = "productId", required = false) Long productId
    ) throws MapperException, MediaUploadFailedException {
        return ResponseEntity.ok().body(mediaService.uploadMedia(files, fileMetaData, userId, productId));
    }

}

package africa.springCore.martbackend.portfolio.system.api;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.core.domain.dtos.response.BioDataResponseDto;
import africa.springCore.martbackend.infrastructure.exception.MapperException;
import africa.springCore.martbackend.infrastructure.exception.MediaUploadFailedException;
import africa.springCore.martbackend.portfolio.system.domain.model.Media;
import africa.springCore.martbackend.portfolio.system.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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


    @Operation(
            summary = "Upload media files",
            description = "API for uploading media files."
    )
    @PostMapping("/upload/products")
    public ResponseEntity<String> uploadProductMedia(
            @RequestParam("file") List<MultipartFile> files,
            @RequestParam("fileMetaData") String fileMetaData,
            @RequestParam(name = "productId", required = false) Long productId
    ) throws MapperException, MediaUploadFailedException {
        return ResponseEntity.ok().body(mediaService.uploadProductMedia(files, fileMetaData, productId));
    }


    @Operation(
            summary = "Retrieve media files",
            description = "API for retrievinng media files."
    )
    @GetMapping("")
    public ResponseEntity<BasePageableResponse<Media>> retrieveAllMedia(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(value = "ownerId", required = false) Long ownerId,
            @RequestParam(name = "documentType", required = false) String documentType
    ) throws MapperException, MediaUploadFailedException {
        return ResponseEntity.ok().body(mediaService.retrieveAllMedia(ownerId, documentType, pageable));
    }

}

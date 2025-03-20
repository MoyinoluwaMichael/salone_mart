package africa.springCore.martbackend.infrastructure.cloudservice.storageservice.service;

import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryUploadService {
    Map uploadFile(MultipartFile file, String folderName) throws Exception;
    Map deleteFile(String publicId) throws Exception;
    String getOptimizedUrl(String publicId, int width, int height, String crop) throws Exception;
}

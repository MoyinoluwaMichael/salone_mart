package africa.springCore.martbackend.infrastructure.cloudservice.storageservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryUploadServiceImpl implements CloudinaryUploadService {
    private final Cloudinary cloudinary;

    @Override
    public Map uploadFile(MultipartFile file, String folderName) throws Exception {
        try {
            Map<String, Object> options = new HashMap<>();
            if (folderName != null && !folderName.isEmpty()) {
                options.put("folder", folderName);
            }
            return cloudinary.uploader().upload(file.getBytes(), options);
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    @Override
    public Map deleteFile(String publicId) throws Exception {
        try {
            return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("File deletion failed", e);
        }
    }

    @Override
    public String getOptimizedUrl(String publicId, int width, int height, String crop) throws Exception {
        return cloudinary.url()
                .transformation(new com.cloudinary.Transformation()
                        .width(width)
                        .height(height)
                        .crop(crop)
                        .fetchFormat("auto")
                        .quality("auto"))
                .generate(publicId);
    }
}

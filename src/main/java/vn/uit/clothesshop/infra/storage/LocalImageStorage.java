package vn.uit.clothesshop.infra.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.annotation.Nullable;
import jakarta.servlet.ServletContext;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class LocalImageStorage /* implements ImageStoragePort */ {
    private static final String IMAGE_FOLDER_PATH = "/resources/img";

    @NotNull
    private final Path imagesRoot;
   
    String cloudinaryURL="cloudinary://981316691563687:C-p2uGPu7XxodPu9_jf5n9_wD4g@drlr2usf0";
    private final Cloudinary cloudinary = new Cloudinary(cloudinaryURL);
    public LocalImageStorage(@NotNull final ServletContext servletContext) {
        this.imagesRoot=null;
    }

    @Nullable
    public String handleSaveUploadFile(final MultipartFile avatarFile,
            @NotBlank final String targetSubFolder) {
        if ((avatarFile == null) || avatarFile.isEmpty()) {
            return null;
        }
        

        try {
            
            Map imageMap = ObjectUtils.asMap(
                "folder", targetSubFolder,  
                "resource_type", "auto"
            );
            Map result = cloudinary.uploader().upload(avatarFile.getBytes(), imageMap);

            return (String) result.get("secure_url");
        } catch (IOException e) {
            log.error("Cannot write file: {}");
            return null;
        }
    }

    

    public void handleDeleteUploadFile(
            @NotBlank final String fileName,
            @NotBlank final String targetSubFolder) {
        final Path file = resolve(targetSubFolder, fileName);
        try {
            if (!Files.deleteIfExists(file)) {
                log.warn("File does not exist: {}", file);
            }
        } catch (IOException e) {
            log.error("Cannot delete file: {}", file, e);
        }
    }

    @Nullable
    public String getPathString(@Nullable final String fileName,
            @NotBlank final String targetSubFolder) {
        if ((fileName == null) || fileName.isBlank()) {
            return null;
        }

        return  fileName;
    }

    public Path getPath(@Nullable final String fileName,
            @NotBlank final String targetSubFolder) {
        if (fileName == null || fileName.isBlank()) {
            return ensureSubDir(targetSubFolder);
        }

        return resolve(targetSubFolder, fileName);
    }

    private Path resolve(@NotBlank final String subFolder, @NotBlank final String fileName) {
        final var file = ensureSubDir(subFolder).resolve(fileName).normalize();
        if (!file.startsWith(imagesRoot)) {
            throw new IllegalArgumentException("Invalid path traversal attempt");
        }

        return file;
    }

    private Path ensureSubDir(@NotBlank final String subFolder) {
        final var safe = sanitizeSegment(subFolder);
        final var dir = imagesRoot.resolve(safe).normalize();
        if (!dir.startsWith(imagesRoot)) {
            throw new IllegalArgumentException("Invalid subFolder: " + subFolder);
        }
        return dir;
    }

    private static String buildStoredFileName(@Nullable final String originalName) {
        final var base = sanitizeFilename(originalName == null ? "file" : originalName);
        return System.currentTimeMillis() + "-" + base;
    }

    private static String sanitizeFilename(@NotNull String name) {
        // Bỏ mọi path component và ký tự lạ
        var clean = name.replace("\\", "/");
        clean = clean.substring(clean.lastIndexOf('/') + 1);
        clean = clean.replaceAll("[\\r\\n\\t]", "");
        clean = clean.replaceAll("[^A-Za-z0-9._-]", "_");
        clean = clean.replaceAll("_+", "_");
        return clean;
    }

    private static String sanitizeSegment(@NotNull String segment) {
        var s = segment.replace("\\", "/");
        s = s.replaceAll("[/]+", "/");
        s = s.replaceAll("^/+", "").replaceAll("/+$", "");
        s = s.replaceAll("[^A-Za-z0-9._/-]", "_");
        return s;
    }

    private void deleteQuietly(String fileName, String subFolder) {
        try {
            handleDeleteUploadFile(fileName, subFolder);
        } catch (Exception e) {
            log.warn("Failed to delete old file '{}' in '{}'", fileName, subFolder, e);
        }
    }
    public String getPublicIdFromUrl(String url) {
    try {
        if (url == null || url.isEmpty()) {
            return null;
        }

       
        String splitToken = "upload/";
        int splitIndex = url.indexOf(splitToken);
        
        if (splitIndex == -1) {
           
            return null; 
        }

       
        String pathAfterUpload = url.substring(splitIndex + splitToken.length());

        
        int slashIndex = pathAfterUpload.indexOf("/");
        if (pathAfterUpload.startsWith("v") && slashIndex > -1) {
            
            pathAfterUpload = pathAfterUpload.substring(slashIndex + 1);
        }
        

        
        int dotIndex = pathAfterUpload.lastIndexOf(".");
        if (dotIndex > -1) {
            pathAfterUpload = pathAfterUpload.substring(0, dotIndex);
        }

       
        return pathAfterUpload;

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

}

/*
 * @Component
 * 
 * @Profile("local")
 * class LocalImageStorage implements ImageStoragePort {
 * private final Path root = Paths.get("uploads");
 * 
 * @Override
 * public String buildPath(String fileName, String subFolder) { // join +
 * sanitize
 * }
 * 
 * @Override
 * public String upload(InputStream in, long size, String contentType, String
 * targetPath) {
 * var path = root.resolve(targetPath);
 * Files.createDirectories(path.getParent());
 * Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
 * return targetPath;
 * }
 * 
 * @Override
 * public void delete(String targetPath) {
 * Files.deleteIfExists(root.resolve(targetPath));
 * }
 * 
 * @Override
 * public String publicUrl(String targetPath) { return "/static/" + targetPath;
 * }
 * }
 * 
 */
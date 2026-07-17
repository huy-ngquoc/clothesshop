package vn.uit.clothesshop.infra.storage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.Nullable;
import jakarta.servlet.ServletContext;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LocalImageStorage /* implements ImageStoragePort */ {
    private final Dotenv dotenv = Dotenv.load();
    private final Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));

    @Nullable
    public String handleSaveUploadFile(final MultipartFile avatarFile,
            @NotBlank final String targetSubFolder) {
        if ((avatarFile == null) || avatarFile.isEmpty()) {
            return null;
        }

        try {
            Map imageMap = ObjectUtils.asMap(
                    "folder", targetSubFolder,
                    "resource_type", "auto");
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

        return fileName;
    }
}

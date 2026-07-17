package vn.uit.clothesshop.shared.storage;

import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public interface ImageStoragePort {
    @Nullable
    String save(final MultipartFile file,
            @NotBlank final String targetSubFolder);

    @Nullable
    void delete(
            @NotBlank final String fileName,
            @NotBlank final String targetSubFolder);

    @Nullable
    String getPathString(
            @Nullable final String fileName,
            @NotBlank final String targetSubFolder);
}

package vn.uit.clothesshop.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Nullable;
import jakarta.servlet.ServletContext;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageUploadService {
    private static final String IMAGE_FOLDER_PATH = "/resources/img";

    @NotNull
    private final ServletContext servletContext;

    public ImageUploadService(
            @NotNull final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Nullable
    public String handleSaveUploadFile(
            final MultipartFile avatarFile,
            @NotBlank final String targetSubFolder) {
        final var rootPath = this.servletContext.getRealPath(IMAGE_FOLDER_PATH);
        final var dir = new File(rootPath + File.separator + targetSubFolder);

        return ImageUploadService.handleSaveUpdateFile(avatarFile, dir);
    }

    @Nullable
    public String handleUpdateUploadFile(
            @NotBlank final String oldFileName,
            final MultipartFile avatarFile,
            @NotBlank final String targetSubFolder) {
        final var rootPath = this.servletContext.getRealPath(IMAGE_FOLDER_PATH);
        final var dir = new File(rootPath + File.separator + targetSubFolder);

        final var newFileName = ImageUploadService.handleSaveUpdateFile(avatarFile, dir);
        if (newFileName != null) {
            final var oldFile = new File(dir.getAbsolutePath() + File.separator + oldFileName);
            try {
                Files.delete(oldFile.toPath());
            } catch (final Exception exception) {
                log.error(String.format("Cannot delete file/folder: %s", oldFile.getAbsolutePath()), exception);
            }
        }

        return newFileName;
    }

    public void handleDeleteUploadFile(
            @NotBlank final String fileName,
            @NotBlank final String targetSubFolder) {
        final var rootPath = this.servletContext.getRealPath(IMAGE_FOLDER_PATH);
        final var dir = new File(rootPath + File.separator + targetSubFolder);
        final var file = new File(dir.getAbsolutePath() + File.separator + fileName);

        try {
            Files.delete(file.toPath());
        } catch (final Exception exception) {
            log.error(String.format("Cannot delete file/folder: %s", file.getAbsolutePath()), exception);
        }
    }

    @Nullable
    private static String handleSaveUpdateFile(
            final MultipartFile avatarFile,
            @NotNull final File targetSubFolder) {
        if (avatarFile.isEmpty()) {
            return null;
        }

        final var newFileName = targetSubFolder.getAbsolutePath()
                + File.separator
                + System.currentTimeMillis()
                + "-"
                + avatarFile.getOriginalFilename();

        final var newFile = new File(newFileName);

        try {
            if (!targetSubFolder.exists()) {
                targetSubFolder.mkdirs();
            }

            final var bytes = avatarFile.getBytes();

            try (final var stream = new BufferedOutputStream(new FileOutputStream(newFile))) {
                stream.write(bytes);
            }

            return newFile.getName();
        } catch (final Exception exception) {
            log.error(String.format("Cannot create file/folder: %s", newFile.getAbsolutePath()), exception);
        }

        return null;
    }
}

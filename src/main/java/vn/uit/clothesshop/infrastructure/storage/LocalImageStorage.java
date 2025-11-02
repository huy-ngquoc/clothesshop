package vn.uit.clothesshop.infrastructure.storage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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

    public LocalImageStorage(@NotNull final ServletContext servletContext) {
        final String realPath = servletContext.getRealPath(IMAGE_FOLDER_PATH);
        if (realPath == null) {
            throw new IllegalStateException("Cannot resolve real path for: " + IMAGE_FOLDER_PATH);
        }
        this.imagesRoot = Paths.get(realPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.imagesRoot);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot create image root: " + this.imagesRoot, e);
        }
    }

    @Nullable
    public String handleSaveUploadFile(final MultipartFile avatarFile,
            @NotBlank final String targetSubFolder) {
        if ((avatarFile == null) || avatarFile.isEmpty()) {
            return null;
        }
        final Path dir = ensureSubDir(targetSubFolder);
        final String storedName = buildStoredFileName(avatarFile.getOriginalFilename());
        final Path target = dir.resolve(storedName);

        try {
            Files.createDirectories(dir);
            Files.copy(avatarFile.getInputStream(), target);
            log.debug("Saved file to {}", target);
            return storedName;
        } catch (IOException e) {
            log.error("Cannot write file: {}", target, e);
            return null;
        }
    }

    @Nullable
    public String handleUpdateUploadFile(
            @NotBlank final String oldFileName,
            final MultipartFile avatarFile,
            @NotBlank final String targetSubFolder) {
        final String newFileName = handleSaveUploadFile(avatarFile, targetSubFolder);
        if (newFileName != null) {
            deleteQuietly(oldFileName, targetSubFolder);
        }
        return newFileName;
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

        return sanitizeSegment(targetSubFolder) + "/" + fileName;
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
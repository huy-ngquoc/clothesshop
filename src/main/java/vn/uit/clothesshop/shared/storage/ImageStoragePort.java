package vn.uit.clothesshop.shared.storage;

import java.io.InputStream;

public interface ImageStoragePort {
    String buildPath(String fileName, String subFolder);

    String upload(InputStream content, long size, String contentType, String targetPath);

    void delete(String targetPath);

    String publicUrl(String targetPath);
}

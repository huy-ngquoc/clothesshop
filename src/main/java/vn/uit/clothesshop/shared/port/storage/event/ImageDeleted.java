package vn.uit.clothesshop.shared.port.storage.event;

import vn.uit.clothesshop.shared.port.storage.ImageFolder;

public record ImageDeleted(
        String fileName,
        ImageFolder folder) {
}

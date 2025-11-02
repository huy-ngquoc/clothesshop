package vn.uit.clothesshop.shared.storage.event;

import vn.uit.clothesshop.shared.storage.ImageFolder;

public record ImageDeleted(
        String fileName,
        ImageFolder folder) {
}

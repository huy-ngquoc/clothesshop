package vn.uit.clothesshop.shared.port.storage.event;

import vn.uit.clothesshop.shared.port.storage.ImageFolder;

public record ImageAdded(
        String fileName,
        ImageFolder folder) {
}

package vn.uit.clothesshop.shared.port.storage.event;

import vn.uit.clothesshop.shared.port.storage.ImageFolder;

public record ImageReplaced(
        String oldFileName,
        String newFileName,
        ImageFolder folder) {
}

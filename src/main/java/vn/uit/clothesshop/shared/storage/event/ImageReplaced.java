package vn.uit.clothesshop.shared.storage.event;

import vn.uit.clothesshop.shared.storage.ImageFolder;

public record ImageReplaced(
        String oldFileName,
        String newFileName,
        ImageFolder folder) {
}

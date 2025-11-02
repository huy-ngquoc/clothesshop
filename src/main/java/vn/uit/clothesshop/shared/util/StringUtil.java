package vn.uit.clothesshop.shared.util;

import jakarta.annotation.Nullable;

public final class StringUtil {
    private StringUtil() {
    }

    @Nullable
    public static String trimOrNull(@Nullable String str) {
        if (str == null) {
            return null;
        }

        return str.trim();
    }
}

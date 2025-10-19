package vn.uit.clothesshop.utils;

import java.util.EnumSet;
import java.util.Set;

import jakarta.validation.constraints.NotNull;

public final class EnumSetHelper {
    private EnumSetHelper() {
    }

    public static <T extends Enum<T>> EnumSet<T> copyOf(
            @NotNull Set<T> source,
            @NotNull Class<T> enumClass) {
        if (source.isEmpty()) {
            return EnumSet.noneOf(enumClass);
        }

        return EnumSet.copyOf(source);
    }
}

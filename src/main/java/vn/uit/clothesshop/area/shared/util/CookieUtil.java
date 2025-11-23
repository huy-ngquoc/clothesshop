package vn.uit.clothesshop.area.shared.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import jakarta.servlet.http.HttpServletRequest;

public final class CookieUtil {
    private CookieUtil() {
    }

    @Nullable
    public static String resolveCookie(@NonNull HttpServletRequest request, @NonNull String name) {
        final var cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (final var c : cookies) {
            if (name.equals(c.getName())) {
                return c.getValue();
            }
        }

        return null;
    }
}

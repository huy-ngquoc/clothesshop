package vn.uit.clothesshop.area.shared.util;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.uit.clothesshop.area.shared.auth.JwtTokenProvider;

public final class CookieUtil {
    public static final String ACCESS_TOKEN_NAME = "ACCESS_TOKEN";
    public static final String REFRESH_TOKEN_NAME = "REFRESH_TOKEN";

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

    public static void addAuthCookies(
            @NonNull final HttpServletResponse response,
            @NonNull final String accessToken,
            @NonNull final String refreshToken) {
        CookieUtil.setCookie(response, ACCESS_TOKEN_NAME, accessToken, JwtTokenProvider.ACCESS_TOKEN_DURATION);
        CookieUtil.setCookie(response, REFRESH_TOKEN_NAME, refreshToken, JwtTokenProvider.REFRESH_TOKEN_DURATION);
    }

    public static void clearAuthCookies(@NonNull final HttpServletResponse response) {
        CookieUtil.setCookie(response, ACCESS_TOKEN_NAME, "", Duration.ZERO);
        CookieUtil.setCookie(response, REFRESH_TOKEN_NAME, "", Duration.ZERO);
    }

    private static void setCookie(
            HttpServletResponse response,
            @NonNull String name,
            String value,
            Duration maxAge) {

        final var cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(maxAge.toSeconds())
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}

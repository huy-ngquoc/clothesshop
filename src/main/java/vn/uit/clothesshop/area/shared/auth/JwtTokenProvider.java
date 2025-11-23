package vn.uit.clothesshop.area.shared.auth;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.shared.resource.ResourceString;

public final class JwtTokenProvider {
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(15);
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(30);

    private static final int MIN_LENGTH_JWT_RAW_KEY = 32;
    private static final MacAlgorithm MAC_ALGORITHM = Jwts.SIG.HS256;
    private static final String DEFAULT_JWT_RAW_KEY = "your-very-secure-secret-32bytes-minimum123!";

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            JwtTokenProvider.getRawSecret().getBytes(StandardCharsets.UTF_8));

    static {
        assert REFRESH_TOKEN_DURATION.minus(ACCESS_TOKEN_DURATION).isPositive();
        assert DEFAULT_JWT_RAW_KEY.length() >= MIN_LENGTH_JWT_RAW_KEY;
    }

    private JwtTokenProvider() {
    }

    public static boolean isTokenValid(@NonNull String token) {
        try {
            parseAllClaims(token);
            return true;
        } catch (final ExpiredJwtException _) {
            return false;
        } catch (final JwtException _) {
            return false;
        }
    }

    @Nullable
    public static String extractUsernameFromToken(@Nullable String token) {
        if (token == null) {
            return null;
        }

        try {
            return parseAllClaims(token).getSubject();
        } catch (final ExpiredJwtException _) {
            return null;
        } catch (final JwtException _) {
            return null;
        }
    }

    public static String generateAccessToken(
            @NotNull final String username,
            @NotNull final Iterable<? extends GrantedAuthority> permissions) {
        final var now = Instant.now();
        final var expiry = now.plus(ACCESS_TOKEN_DURATION);

        return Jwts.builder()
                .subject(username)
                .claim("permissions", permissions)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(SECRET_KEY, MAC_ALGORITHM)
                .compact();
    }

    public static String generateRefreshToken(@NotNull final String username) {
        final var now = Instant.now();
        final var expiry = now.plus(REFRESH_TOKEN_DURATION);

        return Jwts.builder()
                .subject(username)
                .id(UUID.randomUUID().toString())
                .claim("type", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(SECRET_KEY, MAC_ALGORITHM)
                .compact();
    }

    private static Claims parseAllClaims(@NonNull final String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build()
                .parseSignedClaims(token).getPayload();
    }

    private static String getRawSecret() {
        final var rawKey = ResourceString.JWT_RAW_KEY;
        if ((rawKey == null) || rawKey.isEmpty()) {
            return DEFAULT_JWT_RAW_KEY;
        }

        if (rawKey.length() < MIN_LENGTH_JWT_RAW_KEY) {
            throw new IllegalArgumentException(
                    "JWT_RAW_KEY is NOT long enough. Minimum length: " + MIN_LENGTH_JWT_RAW_KEY);
        }

        return ResourceString.JWT_RAW_KEY;
    }
}

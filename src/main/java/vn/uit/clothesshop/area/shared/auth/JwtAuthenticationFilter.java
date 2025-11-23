package vn.uit.clothesshop.area.shared.auth;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.uit.clothesshop.area.shared.util.CookieUtil;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain)
            throws ServletException, IOException {
        final var accessToken = CookieUtil.resolveCookie(request, "ACCESS_TOKEN");
        var username = JwtTokenProvider.extractUsernameFromToken(accessToken);
        if (username != null) {
            final var userDetails = this.userDetailsService.loadUserByUsername(username);
            setAuthenticationForUser(userDetails);

            filterChain.doFilter(request, response);
            return;
        }

        final var refreshToken = CookieUtil.resolveCookie(request, "REFRESH_TOKEN");
        username = JwtTokenProvider.extractUsernameFromToken(refreshToken);
        if (username == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final var userDetails = this.userDetailsService.loadUserByUsername(username);
        final var newAccessToken = JwtTokenProvider.generateAccessToken(username, userDetails.getAuthorities());
        final var newRefreshToken = JwtTokenProvider.generateRefreshToken(username);

        final var accessCookie = ResponseCookie.from("ACCESS_TOKEN", newAccessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(JwtTokenProvider.ACCESS_TOKEN_DURATION.toSeconds())
                .sameSite("Lax")
                .build();

        final var refreshCookie = ResponseCookie.from("REFRESH_TOKEN", newRefreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(JwtTokenProvider.REFRESH_TOKEN_DURATION.toSeconds())
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        setAuthenticationForUser(userDetails);
    }

    private static void setAuthenticationForUser(UserDetails userDetails) {
        final var auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}

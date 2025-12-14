package vn.uit.clothesshop.area.shared.auth;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.uit.clothesshop.area.shared.util.CookieUtil;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        return path.startsWith("/error")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/img/")
                || path.equalsIgnoreCase("/favicon.ico")
                || path.equalsIgnoreCase("/");
    }

    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain)
            throws ServletException, IOException {
        try {
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

            CookieUtil.addAuthCookies(response, newAccessToken, newRefreshToken);

            setAuthenticationForUser(userDetails);
        } catch (final AuthenticationException exception) {
            CookieUtil.clearAuthCookies(response);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private static void setAuthenticationForUser(UserDetails userDetails) {
        final var auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}

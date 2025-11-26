package vn.uit.clothesshop.config.security;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.uit.clothesshop.feature.user.domain.User.Role;

public class SuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    public void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        String targetURL = determineTargetURL(authentication);
        if (!response.isCommitted()) {
            redirectStrategy.sendRedirect(request, response, targetURL);
        }
    }

    public String determineTargetURL(final Authentication authentication) {
        Map<Role, String> roleTargetUrlMap = new HashMap<>();
        roleTargetUrlMap.put(Role.USER, "/");
        roleTargetUrlMap.put(Role.ADMIN, "/admin");
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            if (roleTargetUrlMap.containsKey(grantedAuthority)) {
                return roleTargetUrlMap.get(grantedAuthority);
            }
        }
        throw new IllegalStateException();
    }

    public void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}

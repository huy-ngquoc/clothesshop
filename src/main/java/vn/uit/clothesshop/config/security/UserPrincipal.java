package vn.uit.clothesshop.config.security;

import java.util.Collection;
import java.util.EnumSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import vn.uit.clothesshop.feature.user.domain.User.Role;

public final class UserPrincipal implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final long id;
    private final String username;
    private final String hashedPassword;
    private final Role role;
    private final boolean enabled;

    public UserPrincipal(
            final long id,
            final String username,
            final String hashedPassword,
            final Role role,
            final boolean enabled) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.role = role;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return EnumSet.of(this.role);
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.hashedPassword;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public long getId() {
        return this.id;
    }
}

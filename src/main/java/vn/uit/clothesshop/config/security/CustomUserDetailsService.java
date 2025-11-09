package vn.uit.clothesshop.config.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.uit.clothesshop.shared.util.Message;
import vn.uit.clothesshop.user.domain.User;
import vn.uit.clothesshop.user.domain.UserAccess;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserAccess userService;

    public CustomUserDetailsService(final UserAccess userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(Message.userNotFound));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getHashedPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }

}

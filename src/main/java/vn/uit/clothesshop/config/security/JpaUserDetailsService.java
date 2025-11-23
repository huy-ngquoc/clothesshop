package vn.uit.clothesshop.config.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.uit.clothesshop.feature.user.domain.User;
import vn.uit.clothesshop.feature.user.domain.port.UserReadPort;
import vn.uit.clothesshop.shared.util.Message;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    private final UserReadPort userService;

    public JpaUserDetailsService(final UserReadPort userService) {
        this.userService = userService;
    }

    @Override
    public UserPrincipal loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(Message.userNotFound));

        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getHashedPassword(),
                user.getRole(),
                true);
    }

}

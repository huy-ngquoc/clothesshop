package vn.uit.clothesshop.config.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.uit.clothesshop.feature.user.domain.User;
import vn.uit.clothesshop.feature.user.domain.port.UserReadPort;
import vn.uit.clothesshop.shared.util.Message;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    private final UserReadPort userReadPort;

    public JpaUserDetailsService(final UserReadPort userReadPort) {
        this.userReadPort = userReadPort;
    }

    @Override
    public UserPrincipal loadUserByUsername(final String loginInput) throws UsernameNotFoundException {
        User user = null;

        if (loginInput.contains("@")) {
            user = userReadPort.findByEmail(loginInput)
                    .orElseThrow(() -> new UsernameNotFoundException(Message.userNotFound));
        } else {
            user = userReadPort.findByUsername(loginInput)
                    .orElseThrow(() -> new UsernameNotFoundException(Message.userNotFound));
        }

        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getHashedPassword(),
                user.getRole(),
                true);
    }

}

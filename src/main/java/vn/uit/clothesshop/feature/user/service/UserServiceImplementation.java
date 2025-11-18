package vn.uit.clothesshop.feature.user.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import vn.uit.clothesshop.feature.user.domain.User;
import vn.uit.clothesshop.feature.user.domain.port.UserReadPort;


@Service
public class UserServiceImplementation implements UserService {
    private final UserReadPort userReadPort;
    public UserServiceImplementation(UserReadPort userReadPort) {
        this.userReadPort = userReadPort;
    }
    @Override
    public User getUserFromAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user;
        Object principal = auth.getPrincipal();
        if (auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) principal;
        user = userReadPort.findByEmail(u.getUsername()).orElse(null);
        return user;
    }


}

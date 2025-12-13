package vn.uit.clothesshop.feature.user.service;

import org.springframework.security.core.Authentication;
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
    public User getUserFromAuth(Authentication auth) {
        if(auth==null) {
            return null;
        }
       String username = auth.getName();
        User user = userReadPort.findByUsername(username).orElse(null);
        return user;
    }


}

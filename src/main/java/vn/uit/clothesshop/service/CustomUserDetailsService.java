package vn.uit.clothesshop.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.uit.clothesshop.domain.entity.User;
import vn.uit.clothesshop.repository.UserRepository;
import vn.uit.clothesshop.utils.Message;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByEmail(username);
        if(user==null) {
            throw new UsernameNotFoundException(Message.userNotFound);
        }
        return new org.springframework.security.core.userdetails.User(user.getFullName(), user.getHashedPassword(),
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+user.getRole().name())));
    }

}

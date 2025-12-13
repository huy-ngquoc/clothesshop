package vn.uit.clothesshop.feature.user.service;

import org.springframework.security.core.Authentication;

import vn.uit.clothesshop.feature.user.domain.User;

public interface UserService {
    public User getUserFromAuth(Authentication auth);
}

package vn.uit.clothesshop.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import vn.uit.clothesshop.domain.entity.User;
import vn.uit.clothesshop.dto.request.RegisterDto;
@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    public  User getUserFromRegisterDto(RegisterDto registerDto) {
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setUsername(registerDto.getFirstName()+" "+registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        String hashedPassword = passwordEncoder.encode(registerDto.getPassword());
        user.setHashedPassword(hashedPassword);
        user.setPhoneNumber(registerDto.getPhone());
        return user;
    }
}

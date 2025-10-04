package vn.uit.clothesshop.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountRegistrationRequestDto {
    private final String username;
    private final String password;
    private final String email;
    private final String phoneNumber;

    @JsonCreator
    public AccountRegistrationRequestDto(
            final String username,
            final String password,
            final String email,
            final String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }
}

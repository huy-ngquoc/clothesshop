package vn.uit.clothesshop.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.experimental.FieldNameConstants;

@Entity
@FieldNameConstants
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0;

    @NotBlank
    @Size(min = 2)
    private String username = "";

    @NotBlank
    @Size(max = 256)
    private String hashedPassword = "";

    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Size(max = 128)
    private String email = "";

    @NotBlank
    private String phoneNumber = "";

    @NotBlank
    private String avatarFileName = "";

    User() {
    }

    public User(
            final String username,
            final String hashedPassword,
            final String email,
            final String phoneNumber,
            final String avatarFileName) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatarFileName = avatarFileName;
    }

    public long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public void setHashedPassword(final String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatarFileName() {
        return this.avatarFileName;
    }

    public void setAvatarFileName(final String avatarFileName) {
        this.avatarFileName = avatarFileName;
    }

    void setId(final long id) {
        this.id = id;
    }
}

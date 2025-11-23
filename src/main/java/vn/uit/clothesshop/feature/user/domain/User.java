package vn.uit.clothesshop.feature.user.domain;

import java.time.Instant;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import vn.uit.clothesshop.config.security.SecurityConfig;

@Entity
@EntityListeners(AuditingEntityListener.class)
@FieldNameConstants
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    public enum Role implements GrantedAuthority {
        USER,
        ADMIN,
        ;

        @Override
        public final String getAuthority() {
            return "ROLE_" + this.name();
        }
    }

    public static final int MIN_LENGTH_USERNAME = 2;
    public static final int MAX_LENGTH_USERNAME = 50;
    public static final int MIN_LENGTH_FIRST_NAME = 3;
    public static final int MAX_LENGTH_FIRST_NAME = 20;
    public static final int MIN_LENGTH_LAST_NAME = 3;
    public static final int MAX_LENGTH_LAST_NAME = 20;
    public static final String EMAIL_REG_EXP = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final int MAX_LENGTH_EMAIL = 128;
    public static final int MIN_LENGTH_PHONE_NUMBER = 5;
    public static final int MAX_LENGTH_PHONE_NUMBER = 15;
    public static final int MAX_LENGTH_AVATAR_FILE_NAME = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id = 0;

    @NotBlank
    @Size(min = MIN_LENGTH_USERNAME, max = MAX_LENGTH_USERNAME)
    private String username = "";

    @NotBlank
    @Size(max = SecurityConfig.BCRYPT_ENCODE_LENGTH)
    private String hashedPassword = "";

    @NotBlank
    @Size(min = MIN_LENGTH_FIRST_NAME, max = MAX_LENGTH_FIRST_NAME)
    private String firstName = "";

    @NotBlank
    @Size(min = MIN_LENGTH_LAST_NAME, max = MAX_LENGTH_LAST_NAME)
    private String lastName = "";

    @NotBlank
    @Email(regexp = EMAIL_REG_EXP)
    @Size(max = MAX_LENGTH_EMAIL)
    private String email = "";

    @NotBlank
    @Size(min = MIN_LENGTH_PHONE_NUMBER, max = MAX_LENGTH_PHONE_NUMBER)
    private String phoneNumber = "";

    @Nullable
    @Size(max = MAX_LENGTH_AVATAR_FILE_NAME)
    private String avatarFileName = null;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @CreatedDate
    @NotNull
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @LastModifiedDate
    @NotNull
    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    public User() {
    }

    public User(
            final String username,
            final String hashedPassword,
            final String firstName,
            final String lastName,
            final String email,
            final String phoneNumber,
            final Role role) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
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

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return String.format(
                "%s %s",
                this.firstName,
                this.lastName);
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

    public Role getRole() {
        return this.role;
    }

    public void setRole(final Role role) {
        this.role = role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    void setId(final long id) {
        this.id = id;
    }

    void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}

package vn.uit.clothesshop.user.presentation.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.uit.clothesshop.user.domain.User;
import vn.uit.clothesshop.user.domain.User.Role;

public final class UserCreationForm {
    @NotBlank
    @Size(min = User.MIN_LENGTH_USERNAME, max = User.MAX_LENGTH_USERNAME)
    private String username = "";

    @NotNull
    @Size(min = 3, max = 72)
    private String password = "";

    @NotBlank
    @Size(min = User.MIN_LENGTH_FIRST_NAME, max = User.MAX_LENGTH_FIRST_NAME)
    private String firstName = "";

    @NotBlank
    @Size(min = User.MIN_LENGTH_LAST_NAME, max = User.MAX_LENGTH_LAST_NAME)
    private String lastName = "";

    @NotBlank
    @Email(regexp = User.EMAIL_REG_EXP)
    @Size(max = User.MAX_LENGTH_EMAIL)
    private String email = "";

    @NotBlank
    @Size(min = User.MIN_LENGTH_PHONE_NUMBER, max = User.MAX_LENGTH_PHONE_NUMBER)
    private String phoneNumber = "";

    @NotNull
    private Role role = Role.USER;

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Role getRole() {
        return this.role;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRole(final Role role) {
        this.role = role;
    }

}

package vn.uit.clothesshop.feature.user.presentation.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.experimental.FieldNameConstants;
import vn.uit.clothesshop.feature.user.domain.User;
import vn.uit.clothesshop.shared.validation.EmailExist;
import vn.uit.clothesshop.shared.validation.PasswordMatches;
import vn.uit.clothesshop.shared.validation.StrongPassword;

@PasswordMatches(passwordField = RegisterDto.Fields.password, confirmPasswordField = RegisterDto.Fields.confirmPassword, message = "Password and confirm password does not match")
@FieldNameConstants
public class RegisterDto {
    @NotBlank(message = "First name is required")
    @Size(min = User.MIN_LENGTH_FIRST_NAME, max = User.MAX_LENGTH_FIRST_NAME)
    private String firstName = "";
    @NotBlank(message = "Last name is required")
    @Size(min = User.MIN_LENGTH_LAST_NAME, max = User.MAX_LENGTH_LAST_NAME)
    private String lastName = "";
    @NotBlank(message = "Password is required")
    @StrongPassword
    private String password = "";
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword = "";
    @NotBlank(message = "Email is required")

    @Email
    @EmailExist
    private String email = "";

    @NotBlank
    @Size(min = User.MIN_LENGTH_PHONE_NUMBER, max = User.MAX_LENGTH_PHONE_NUMBER)
    private String phone;

    public String getFirstName() {
        return firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

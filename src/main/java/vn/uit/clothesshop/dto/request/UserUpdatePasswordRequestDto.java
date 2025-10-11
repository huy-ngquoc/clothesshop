package vn.uit.clothesshop.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserUpdatePasswordRequestDto {
    @NotNull
    @Size(min = 3, max = 72)
    private String newPassword = "";

    public String getNewPassword() {
        return this.newPassword;
    }

    public void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }

}

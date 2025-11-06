package vn.uit.clothesshop.user.presentation.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;

public final class UserAvatarUpdateForm {
    @NotNull
    private MultipartFile avatarFile = null;

    public MultipartFile getAvatarFile() {
        return avatarFile;
    }

    public void setAvatarFile(final MultipartFile avatarFile) {
        this.avatarFile = avatarFile;
    }
}

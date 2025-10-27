package vn.uit.clothesshop.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;

public final class UserUpdateAvatarRequestDto {
    @NotNull
    private MultipartFile avatarFile = null;

    public MultipartFile getAvatarFile() {
        return avatarFile;
    }

    public void setAvatarFile(final MultipartFile avatarFile) {
        this.avatarFile = avatarFile;
    }
}

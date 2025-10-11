package vn.uit.clothesshop.dto.request;

import org.springframework.web.multipart.MultipartFile;

public final class UserUpdateAvatarRequestDto {
    private MultipartFile avatarFile = null;

    public MultipartFile getAvatarFile() {
        return this.avatarFile;
    }

    public void setAvatarFile(MultipartFile avatarFile) {
        this.avatarFile = avatarFile;
    }

}

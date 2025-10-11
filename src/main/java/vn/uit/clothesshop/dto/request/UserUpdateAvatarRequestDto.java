package vn.uit.clothesshop.dto.request;

public final class UserUpdateAvatarRequestDto {
    private byte[] avatarFile = new byte[] {};

    public byte[] getAvatarFile() {
        return this.avatarFile.clone();
    }

    public void setAvatarFile(final byte[] avatarFile) {
        this.avatarFile = avatarFile.clone();
    }

}

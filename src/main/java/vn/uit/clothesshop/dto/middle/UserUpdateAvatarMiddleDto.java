package vn.uit.clothesshop.dto.middle;

import vn.uit.clothesshop.dto.request.UserUpdateAvatarRequestDto;

public class UserUpdateAvatarMiddleDto {
    private final String avatarFilePath;

    private final UserUpdateAvatarRequestDto requestDto;

    public UserUpdateAvatarMiddleDto(
            final String avatarFilePath,
            final UserUpdateAvatarRequestDto requestDto) {
        this.avatarFilePath = avatarFilePath;
        this.requestDto = requestDto;
    }

    public String getAvatarFilePath() {
        return this.avatarFilePath;
    }

    public UserUpdateAvatarRequestDto getRequestDto() {
        return this.requestDto;
    }

}

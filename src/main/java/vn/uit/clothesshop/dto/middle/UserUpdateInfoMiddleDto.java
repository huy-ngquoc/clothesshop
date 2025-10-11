package vn.uit.clothesshop.dto.middle;

import vn.uit.clothesshop.dto.request.UserUpdateInfoRequestDto;

public class UserUpdateInfoMiddleDto {
    private final String username;
    private final String avatarFilePath;

    private final UserUpdateInfoRequestDto requestDto;

    public UserUpdateInfoMiddleDto(
            final String username,
            final String avatarFilePath,
            final UserUpdateInfoRequestDto requestDto) {
        this.username = username;
        this.avatarFilePath = avatarFilePath;
        this.requestDto = requestDto;
    }

    public String getUsername() {
        return this.username;
    }

    public String getAvatarFilePath() {
        return this.avatarFilePath;
    }

    public UserUpdateInfoRequestDto getRequestDto() {
        return this.requestDto;
    }
}

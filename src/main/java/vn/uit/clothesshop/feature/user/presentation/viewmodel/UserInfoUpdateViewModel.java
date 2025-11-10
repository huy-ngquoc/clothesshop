package vn.uit.clothesshop.feature.user.presentation.viewmodel;

public final class UserInfoUpdateViewModel {
    private final String username;
    private final String avatarFilePath;

    public UserInfoUpdateViewModel(
            final String username,
            final String avatarFilePath) {
        this.username = username;
        this.avatarFilePath = avatarFilePath;
    }

    public String getUsername() {
        return this.username;
    }

    public String getAvatarFilePath() {
        return this.avatarFilePath;
    }
}

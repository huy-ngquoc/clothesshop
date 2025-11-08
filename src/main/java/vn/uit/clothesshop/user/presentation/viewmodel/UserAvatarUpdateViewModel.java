package vn.uit.clothesshop.user.presentation.viewmodel;

public final class UserAvatarUpdateViewModel {
    private final String avatarFilePath;

    public UserAvatarUpdateViewModel(
            final String avatarFilePath) {
        this.avatarFilePath = avatarFilePath;
    }

    public String getAvatarFilePath() {
        return avatarFilePath;
    }

}

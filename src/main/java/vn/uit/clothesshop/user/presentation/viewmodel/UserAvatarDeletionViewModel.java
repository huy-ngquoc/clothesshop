package vn.uit.clothesshop.user.presentation.viewmodel;

public final class UserAvatarDeletionViewModel {
    private final String avatarFilePath;

    public UserAvatarDeletionViewModel(
            final String avatarFilePath) {
        this.avatarFilePath = avatarFilePath;
    }

    public String getAvatarFilePath() {
        return avatarFilePath;
    }
}

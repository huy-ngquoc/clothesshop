package vn.uit.clothesshop.feature.user.presentation.viewmodel;

public final class UserDeletionViewModel {
    private final String avatarFilePath;

    public UserDeletionViewModel(
            final String avatarFilePath) {
        this.avatarFilePath = avatarFilePath;
    }

    public String getAvatarFilePath() {
        return avatarFilePath;
    }

}

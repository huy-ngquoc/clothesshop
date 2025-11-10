package vn.uit.clothesshop.feature.user.presentation.viewmodel;

import vn.uit.clothesshop.feature.user.domain.User.Role;

public final class UserBasicInfoViewModel {
    private final long id;
    private final String username;
    private final String fullName;
    private final Role role;

    public UserBasicInfoViewModel(
            final long id,
            final String username,
            final String fullName,
            final Role role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    public long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Role getRole() {
        return this.role;
    }

}

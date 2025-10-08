package vn.uit.clothesshop.dto.response;

import vn.uit.clothesshop.domain.User.Role;

public final class UserDetailInfoResponseDto {
    private final long id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final String avatarFilePath;
    private final Role role;

    public UserDetailInfoResponseDto(
            final long id,
            final String username,
            final String firstName,
            final String lastName,
            final String email,
            final String phoneNumber,
            final String avatarFilePath,
            final Role role) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatarFilePath = avatarFilePath;
        this.role = role;
    }

    public long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getAvatarFilePath() {
        return this.avatarFilePath;
    }

    public Role getRole() {
        return this.role;
    }

}

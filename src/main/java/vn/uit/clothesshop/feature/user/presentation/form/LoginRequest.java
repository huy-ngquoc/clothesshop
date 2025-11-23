package vn.uit.clothesshop.feature.user.presentation.form;

public final class LoginRequest {
    private String username = "";
    private String password = "";

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.username = email;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String email) {
        this.username = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

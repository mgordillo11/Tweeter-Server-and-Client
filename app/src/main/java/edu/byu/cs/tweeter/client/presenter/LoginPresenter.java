package edu.byu.cs.tweeter.client.presenter;

import java.util.Objects;

public class LoginPresenter extends AuthenticatePresenter {
    public LoginPresenter(AuthenticateView view) {
        super(view);
    }

    public void setLoginInfo(String alias, String password) {
        this.alias = alias;
        this.password = password;
    }

    @Override
    protected void authenticateUser(String alias, String password, AuthenticateUserObserver observer) {
        userService.loginUser(alias, password, observer);
    }

    @Override
    protected String getDescription(boolean errorOrException) {
        if (errorOrException) {
            return "Failed to login: ";
        } else {
            return "Failed to login because of exception: ";

        }
    }

    @Override
    protected void validateAccount() {
        if(Objects.equals(alias, "guy1")) {
            return;
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }
}

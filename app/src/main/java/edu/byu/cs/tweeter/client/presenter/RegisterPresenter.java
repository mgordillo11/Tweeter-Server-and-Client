package edu.byu.cs.tweeter.client.presenter;

public class RegisterPresenter extends AuthenticatePresenter {
    private String firstName;
    private String lastName;
    private String imageBytesBase64;

    public RegisterPresenter(AuthenticateView view) {
        super(view);
    }

    public void setRegistrationInfo(String firstName, String lastName, String alias, String password, String imageBytesBase64) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.password = password;
        this.imageBytesBase64 = imageBytesBase64;
    }

    @Override
    protected void authenticateUser(String alias, String password, AuthenticateUserObserver observer) {
        userService.registerUser(firstName, lastName, alias, password, imageBytesBase64, observer);
    }

    @Override
    protected String getDescription(boolean errorOrException) {
        if (errorOrException) {
            return "Failed to register: ";
        } else {
            return "Failed to register because of exception: ";

        }
    }

    @Override
    protected void validateAccount() {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
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
        if (imageBytesBase64.length() == 0) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }
}

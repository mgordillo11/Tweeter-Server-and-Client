package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.backgroundTask.observer.AuthenticateNotificationObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticatePresenter extends Presenter {
    private final AuthenticateView view;

    protected String alias;
    protected String password;

    public interface AuthenticateView extends View {
        void showAuthenticatedUser(User user, String fullName);

        void setError(String error);

        void setAuthenticatingToast();

        void showAuthenticatingToast(boolean toastStatus);
    }

    public AuthenticatePresenter(AuthenticateView view) {
        this.view = view;
    }

    public void authenticateAccount() {
        try {
            validateAccount();

            view.setError(null);
            view.setAuthenticatingToast();
            view.showAuthenticatingToast(true);

            authenticateUser(alias, password, new AuthenticateUserObserver());
        } catch (Exception e) {
            view.setError(e.getMessage());
        }
    }

    protected abstract void authenticateUser(String alias, String password, AuthenticateUserObserver observer);

    protected abstract String getDescription(boolean errorOrException);

    protected abstract void validateAccount();

    public class AuthenticateUserObserver implements AuthenticateNotificationObserver {

        @Override
        public void handleSuccess(User registeredUser, Authtoken authToken) {
            Cache.getInstance().setCurrUser(registeredUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            view.showAuthenticatedUser(registeredUser, Cache.getInstance().getCurrUser().getName());
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage(getDescription(true) + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage(getDescription(false) + ex.getMessage());
        }
    }
}

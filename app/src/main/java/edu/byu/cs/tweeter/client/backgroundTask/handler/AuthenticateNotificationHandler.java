package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.AuthenticateTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.AuthenticateNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticateNotificationHandler extends BackgroundTaskHandler<AuthenticateNotificationObserver> {
    public AuthenticateNotificationHandler(AuthenticateNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, AuthenticateNotificationObserver observer) {
        User loggedInUser = (User) data.getSerializable(AuthenticateTask.USER_KEY);
        Authtoken authToken = (Authtoken) data.getSerializable(AuthenticateTask.AUTH_TOKEN_KEY);

        observer.handleSuccess(loggedInUser, authToken);
    }
}

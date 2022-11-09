package edu.byu.cs.tweeter.client.backgroundTask.observer;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.User;

public interface AuthenticateNotificationObserver extends ServiceObserver {
    void handleSuccess(User user, Authtoken authToken);
}

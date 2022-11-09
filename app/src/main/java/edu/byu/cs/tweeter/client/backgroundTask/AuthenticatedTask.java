package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.Authtoken;

public abstract class AuthenticatedTask extends BackgroundTask {
    /**
     * Auth token for logged-in user.
     */
    protected Authtoken authToken;

    public AuthenticatedTask(Handler messageHandler, Authtoken authToken) {
        super(messageHandler);
        this.authToken = authToken;
    }
}

package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {
    private static final String LOG_TAG = "FollowTask";

    /**
     * The user that is being followed.
     */
    private User followee;

    public FollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.followee = followee;
    }

    @Override
    protected void processTask() {
        // It simple needs to send a success message but our background task does that
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        // Nothing needs to be added to the bundle for this task
    }
}

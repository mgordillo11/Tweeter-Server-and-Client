package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {
    private static final String LOG_TAG = "UnfollowTask";

    /**
     * The user that is being un followed.
     */
    private User followee;

    public UnfollowTask(Authtoken authToken, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.followee = followee;
    }

    @Override
    protected void processTask() {
        // It simple needs to send a success message but our background task does that
        try {
            UnfollowRequest request = new UnfollowRequest(authToken, followee);
            UnfollowResponse response = getServerFacade().unfollow(request, "/unfollow");

            if (response.isSuccess()) {
                // Nothing needs to be added to the bundle for this task
            } else {
                Log.e(BackgroundTask.MESSAGE_KEY, response.getMessage());
                throw new RuntimeException("[Bad Request] Unable to unfollow user");
            }

        } catch (IOException | TweeterRemoteException ex) {
            ex.printStackTrace();
            Log.e(BackgroundTask.EXCEPTION_KEY, ex.getMessage(), ex);
            throw new RuntimeException("[Server Error] Unable to register user");
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        // Nothing needs to be added to the bundle for this task
    }
}

package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;

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

        try {
            FollowRequest request = new FollowRequest(authToken, followee);
            FollowResponse response = getServerFacade().follow(request, "/follow");

            if(response.isSuccess()) {
                // Nothing needs to be added to the bundle for this task
            } else {
                Log.e(BackgroundTask.MESSAGE_KEY, response.getMessage());
                throw new RuntimeException("[Bad Request] Unable to follow user");
            }
        } catch (IOException | TweeterRemoteException ex) {
            ex.printStackTrace();
            Log.e(BackgroundTask.EXCEPTION_KEY, ex.getMessage(), ex);
            throw new RuntimeException("[Server Error] Unable to follow user");
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        // Nothing needs to be added to the bundle for this task
    }
}

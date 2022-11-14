package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AuthenticatedTask {
    private static final String LOG_TAG = "PostStatusTask";

    /**
     * The new status being sent. Contains all properties of the status,
     * including the identity of the user sending the status.
     */
    private Status status;

    public PostStatusTask(Authtoken authToken, Status status, Handler messageHandler) {
        super(messageHandler, authToken);
        this.status = status;
    }

    @Override
    protected void processTask() {
        try {
            PostStatusRequest request = new PostStatusRequest(authToken, status);
            PostStatusResponse response = getServerFacade().postStatus(request, "/poststatus");

            // It simple needs to send a success message but our background task does that
            if (!response.isSuccess()) {
                Log.e(BackgroundTask.MESSAGE_KEY, response.getMessage());
                throw new RuntimeException("[Bad Request] " + response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            ex.printStackTrace();
            Log.e(BackgroundTask.EXCEPTION_KEY, ex.getMessage(), ex);
            throw new RuntimeException("[Server Error] Unable to post status");
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        // Nothing needs to be added to the bundle for this task
    }
}

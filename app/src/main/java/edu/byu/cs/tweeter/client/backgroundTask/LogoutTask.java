package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {
    private static final String LOG_TAG = "LogoutTask";

    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(messageHandler, authToken);
    }

    @Override
    protected void processTask() {
        // It simple needs to send a success message but our background task does that

        try {
            LogoutRequest request = new LogoutRequest(authToken);
            LogoutResponse response = getServerFacade().logout(request, "/logout");

            if (response.isSuccess()) {
                // Nothing needs to be added to the bundle for this task
            } else {
                Log.e(BackgroundTask.MESSAGE_KEY, response.getMessage());
                throw new RuntimeException("[Bad Request] Unable to logout user");
            }
        } catch (IOException | TweeterRemoteException ex) {
            ex.printStackTrace();
            Log.e(BackgroundTask.EXCEPTION_KEY, ex.getMessage(), ex);
            throw new RuntimeException("[Server Error] Unable to logout user");
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        // Nothing needs to be added to the bundle for this task
    }
}


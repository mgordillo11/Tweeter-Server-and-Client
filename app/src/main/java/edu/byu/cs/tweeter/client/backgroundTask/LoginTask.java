package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {

    private static final String LOG_TAG = "LoginTask";

    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler, username, password);
    }

    @Override
    protected Pair<User, AuthToken> runAuthenticationTask() {

//        User loggedInUser = getFakeData().getFirstUser();
//        AuthToken authToken = getFakeData().getAuthToken();

        try {
            LoginRequest request = new LoginRequest(username, password);
            LoginResponse response = getServerFacade().login(request, "/login");

            if(response.isSuccess()) {
                return new Pair<>(response.getUser(), response.getAuthToken());
            } else {
                throw new RuntimeException("[BadRequest400Exception] Unable to login user");
            }
        } catch (IOException | TweeterRemoteException ex) {
            ex.printStackTrace();
            Log.e(BackgroundTask.EXCEPTION_KEY, ex.getMessage(), ex);
            throw new RuntimeException("[ServerError500Exception] Unable to login user");
        }

        //return new Pair<>(loggedInUser, authToken);
    }
}
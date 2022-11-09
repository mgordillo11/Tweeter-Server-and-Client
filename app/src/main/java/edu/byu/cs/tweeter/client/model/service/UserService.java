package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.AuthenticateNotificationHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.backgroundTask.observer.AuthenticateNotificationObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Authtoken;

public class UserService {
    public void getUserProfile(Authtoken currUserAuthToken, String userAlias, GetUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                userAlias, new GetUserHandler(observer));

        BackgroundTaskUtils.runTask(getUserTask);
    }

    public void registerUser(String firstName, String lastName, String alias, String password, String imageBytesBase64, AuthenticateNotificationObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new AuthenticateNotificationHandler(observer));

        BackgroundTaskUtils.runTask(registerTask);
    }

    public void loginUser(String alias, String password, AuthenticateNotificationObserver observer) {
        LoginTask loginTask = new LoginTask(alias,
                password, new AuthenticateNotificationHandler(observer));

        BackgroundTaskUtils.runTask(loginTask);
    }

    public void logoutUser(Authtoken authToken, SimpleNotificationObserver logoutUserObserver) {
        LogoutTask logoutTask = new LogoutTask(authToken, new SimpleNotificationHandler(logoutUserObserver));

        BackgroundTaskUtils.runTask(logoutTask);
    }
}

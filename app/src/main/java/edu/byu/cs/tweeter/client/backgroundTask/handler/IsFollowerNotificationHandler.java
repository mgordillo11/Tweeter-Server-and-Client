package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.IsFollowerNotificationObserver;

public class IsFollowerNotificationHandler extends BackgroundTaskHandler<IsFollowerNotificationObserver> {
    public IsFollowerNotificationHandler(IsFollowerNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, IsFollowerNotificationObserver observer) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.handleSuccess(isFollower);
    }
}
package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.observer.FollowUserStatusObserver;

public class FollowStatusHandler extends BackgroundTaskHandler<FollowUserStatusObserver> {
    public FollowStatusHandler(FollowUserStatusObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, FollowUserStatusObserver observer) {
        observer.handleSuccess();
    }
}

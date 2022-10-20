package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.observer.CountNotificationObserver;

public class CountNotificationHandler extends BackgroundTaskHandler<CountNotificationObserver> {
    public CountNotificationHandler(CountNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, CountNotificationObserver observer) {
        int count = data.getInt(GetCountTask.COUNT_KEY);
        observer.handleSuccess(count);
    }
}

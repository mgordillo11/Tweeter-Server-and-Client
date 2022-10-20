package edu.byu.cs.tweeter.client.backgroundTask.observer;

import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public abstract class IsFollowerNotificationObserver implements ServiceObserver {
    private final MainPresenter.MainView view;
    private final String failureMessage;
    private final String exceptionMessage;

    public IsFollowerNotificationObserver(MainPresenter.MainView view, String failureMessage, String exceptionMessage) {
        this.view = view;
        this.failureMessage = failureMessage;
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public void handleFailure(String message) {
        view.displayMessage(failureMessage + message);
    }

    @Override
    public void handleException(Exception ex) {
        view.displayMessage(exceptionMessage + ex.getMessage());
    }

    public void handleSuccess(boolean status) {
        view.setFollowButton(status);
    }
}

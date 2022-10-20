package edu.byu.cs.tweeter.client.backgroundTask.observer;

import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public abstract class CountNotificationObserver implements ServiceObserver {
    private final MainPresenter.MainView view;
    private final String failureMessage;
    private final String exceptionMessage;

    public CountNotificationObserver(MainPresenter.MainView view, String failureMessage, String exceptionMessage) {
        this.view = view;
        this.failureMessage = failureMessage;
        this.exceptionMessage = exceptionMessage;
    }

    public abstract void handleSuccess(int count);

    @Override
    public void handleFailure(String message) {
        view.displayMessage(failureMessage + message);

    }

    @Override
    public void handleException(Exception exception) {
        view.displayMessage(exceptionMessage + exception.getMessage());
    }
}

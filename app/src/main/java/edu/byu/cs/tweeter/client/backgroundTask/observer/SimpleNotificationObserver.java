package edu.byu.cs.tweeter.client.backgroundTask.observer;

import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public abstract class SimpleNotificationObserver implements ServiceObserver {
    private final String exceptionMessage;
    private final String failureMessage;
    private final MainPresenter.MainView view;

    public SimpleNotificationObserver(MainPresenter.MainView view, String failureMessage, String exceptionMessage) {
        this.view = view;
        this.exceptionMessage = exceptionMessage;
        this.failureMessage = failureMessage;
    }

    public abstract void handleSuccess();

    @Override
    public void handleFailure(String message) {
        view.displayMessage(failureMessage + message);
    }

    @Override
    public void handleException(Exception exception) {
        view.displayMessage(exceptionMessage + exception.getMessage());
    }
}

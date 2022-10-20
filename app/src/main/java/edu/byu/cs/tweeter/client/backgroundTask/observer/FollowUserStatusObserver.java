package edu.byu.cs.tweeter.client.backgroundTask.observer;

import edu.byu.cs.tweeter.client.presenter.MainPresenter;

public abstract class FollowUserStatusObserver implements ServiceObserver {
    private final MainPresenter.MainView view;
    private final String failureMessage;
    private final String exceptionMessage;

    private final boolean isFollow;

    public FollowUserStatusObserver(MainPresenter.MainView view, String failureMessage, String exceptionMessage, boolean isFollow) {
        this.view = view;
        this.failureMessage = failureMessage;
        this.exceptionMessage = exceptionMessage;
        this.isFollow = isFollow;
    }

    public void handleSuccess() {
        view.updateSelectedUserFollowingAndFollowees();
        view.updateFollowButton(isFollow);
        view.enableFollowButton(true);
    }

    @Override
    public void handleFailure(String message) {
        view.displayMessage(failureMessage + message);
        view.enableFollowButton(true);
    }

    @Override
    public void handleException(Exception exception) {
        view.displayMessage(exceptionMessage + exception.getMessage());
        view.enableFollowButton(true);
    }
}

package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.PageNotificationObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {
    private final PagedView<T> view;
    private T lastItem;

    private static final int PAGE_SIZE = 10;

    private boolean hasMorePages;
    private boolean isLoading;

    public interface PagedView<T> extends View {
        // duplicate page view code
        void setLoadingFooter(boolean footerStatus);

        void displayUserInfo(User user);

        void addItems(List<T> items);
    }

    public PagedPresenter(PagedView<T> view) {
        this.view = view;
    }

    public void loadMoreItems(User targetUser) {
        isLoading = true;
        view.setLoadingFooter(true);

        getItems(Cache.getInstance().getCurrUserAuthToken(), targetUser, PAGE_SIZE, lastItem, new GetItemsObserver());
    }

    public void getUser(String userAlias) {
        userService.getUserProfile(Cache.getInstance().getCurrUserAuthToken(), userAlias, new GetUserProfileObserver());
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    protected abstract void getItems(AuthToken authToken, User user, int pageSize, T lastItem, GetItemsObserver getItemsObserver);

    protected abstract String getDescription(boolean errorOrException);

    protected class GetItemsObserver implements PageNotificationObserver<T> {
        @Override
        public void handleSuccess(List<T> items, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);

            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            view.addItems(items);

            setHasMorePages(hasMorePages);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(false);

            view.displayMessage(getDescription(true) + ": " + message);
        }

        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);

            view.displayMessage(getDescription(false) + ": " + ex.getMessage());
        }
    }

    protected class GetUserProfileObserver implements GetUserObserver {
        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(User user) {
            view.displayUserInfo(user);
        }
    }
}

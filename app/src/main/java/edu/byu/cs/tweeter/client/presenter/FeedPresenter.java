package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {

    public FeedPresenter(PagedView<Status> view) {
        super(view);
    }

    @Override
    protected void getItems(AuthToken authToken, User user, int pageSize, Status lastItem, PagedPresenter<Status>.GetItemsObserver getItemsObserver) {
        statusService.loadMoreFeed(authToken, user, pageSize, lastItem, getItemsObserver);
    }

    @Override
    protected String getDescription(boolean errorOrException) {
        if (errorOrException) {
            return "Failed to get feed: ";
        } else {
            return "Failed to get feed because of exception: ";

        }
    }
}

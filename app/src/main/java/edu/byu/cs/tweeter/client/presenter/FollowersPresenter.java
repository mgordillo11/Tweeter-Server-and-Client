package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {

    public FollowersPresenter(PagedView<User> view) {
        super(view);
    }

    @Override
    protected void getItems(Authtoken authToken, User user, int pageSize, User lastItem, PagedPresenter<User>.GetItemsObserver getItemsObserver) {
        followService.loadMoreFollowers(authToken, user, pageSize, lastItem, getItemsObserver);
    }

    @Override
    protected String getDescription(boolean errorOrException) {
        if (errorOrException) {
            return "Failed to get followers: ";
        } else {
            return "Failed to get followers because of exception: ";

        }
    }
}

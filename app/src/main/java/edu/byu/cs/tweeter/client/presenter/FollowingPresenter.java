package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {

    public FollowingPresenter(PagedView<User> view) {
        super(view);
    }

    @Override
    protected void getItems(Authtoken authToken, User user, int pageSize, User lastItem, GetItemsObserver observer) {
        followService.loadMoreFollowees(authToken, user, pageSize, lastItem, observer);
    }

    @Override
    protected String getDescription(boolean errorOrException) {
        if (errorOrException) {
            return "Failed to get following: ";
        } else {
            return "Failed to get following because of exception: ";

        }
    }
}

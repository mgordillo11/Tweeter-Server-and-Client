package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedTask<User> {
    private static final String LOG_TAG = "GetFollowingTask";

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastFollowee);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        //return getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());

        if (getLastItem() == null) {
            lastItem = getFakeData().getFirstUser();
        }

        try {
            FollowingRequest request = new FollowingRequest(authToken, getTargetUser().getAlias(), getLimit(), getLastItem().getAlias());
            FollowingResponse response = getServerFacade().getFollowees(request, "/getfollowing");

            if (response.isSuccess()) {
                return new Pair<>(response.getFollowees(), response.getHasMorePages());
            } else {
                throw new RuntimeException("[BadRequest400Exception] Unable to retrieve followees");
            }
        } catch (IOException | TweeterRemoteException ex) {
            ex.printStackTrace();
            Log.e(BackgroundTask.EXCEPTION_KEY, ex.getMessage(), ex);
            throw new RuntimeException("[ServerError500Exception] Unable to retrieve followees");
        }
    }
}

package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedTask<User> {
    private static final String LOG_TAG = "GetFollowersTask";


    public GetFollowersTask(Authtoken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastFollower);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {

        try {
            String lastFollowerAlias = lastItem == null ? getTargetUser().getAlias() : lastItem.getAlias();
            FollowersRequest request = new FollowersRequest(authToken, getTargetUser().getAlias(), getLimit(), lastFollowerAlias);
            FollowersResponse response = getServerFacade().getFollowers(request, "/getfollowers");

            if (response.isSuccess()) {
                return new Pair<>(response.getFollowers(), response.getHasMorePages());
            } else {
                throw new RuntimeException("[Bad Request] " + response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            ex.printStackTrace();
            Log.e(BackgroundTask.EXCEPTION_KEY, ex.getMessage(), ex);
            throw new RuntimeException("[Server Error] Unable to retrieve followers");
        }
    }

}

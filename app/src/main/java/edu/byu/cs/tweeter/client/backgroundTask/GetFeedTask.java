package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedTask<Status> {
    private static final String LOG_TAG = "GetFeedTask";

    public GetFeedTask(Authtoken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastStatus);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() {

        try {
            Status lastStatusAlias = getLastItem() == null ? null : getLastItem();

            FeedRequest request = new FeedRequest(authToken, lastStatusAlias, getTargetUser().getAlias(), getLimit());
            FeedResponse response = getServerFacade().getFeed(request, "/getfeed");

            if (response.isSuccess()) {
                return new Pair<>(response.getStatuses(), response.getHasMorePages());
            } else {
                throw new RuntimeException("[Bad Request] Unable to retrieve feed");
            }

        } catch (IOException | TweeterRemoteException ex) {
            ex.printStackTrace();
            Log.e(BackgroundTask.EXCEPTION_KEY, ex.getMessage(), ex);
            throw new RuntimeException("[Server Error] Unable to retrieve feed");
        }
    }
}

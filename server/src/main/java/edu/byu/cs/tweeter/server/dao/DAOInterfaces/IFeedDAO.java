package edu.byu.cs.tweeter.server.dao.DAOInterfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;

public interface IFeedDAO {
    FeedResponse getFeed(FeedRequest request);

    void updateFeed(String alias, PostStatusRequest request);

    void addFeedBatch(Status status, List<String> followers);
}

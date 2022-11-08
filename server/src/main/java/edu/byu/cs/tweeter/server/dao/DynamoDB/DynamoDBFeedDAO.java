package edu.byu.cs.tweeter.server.dao.DynamoDB;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IFeedDAO;

public class DynamoDBFeedDAO implements IFeedDAO {

    @Override
    public FeedResponse getFeed(FeedRequest request) {
        return null;
    }

    @Override
    public void updateFeed(String alias, PostStatusRequest request) {

    }
}

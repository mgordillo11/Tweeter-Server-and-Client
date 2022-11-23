package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.DAOFactory;

public class StatusService {
    private final DAOFactory daoFactory;

    public StatusService(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Status is null");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] AuthToken is null");
        } else if (request.getStatus().post == null) {
            throw new RuntimeException("[Bad Request] Status text is null");
        }

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthToken());
        if (!validAuthtoken) {
            return new PostStatusResponse("Session expired, please log out and log in again");
        }

        boolean statusPosted = daoFactory.getStoryDAO().postStatus(request.getStatus());
        if (!statusPosted) {
            return new PostStatusResponse("An error occurred while posting the status");
        }

        // TODO: Update the feed for all followers
        // FIXED WITH MILESTONE 4B (see UpdateFeedsHandler.java)
//        List<String> followers = daoFactory.getFollowDAO().getFollowersAlias(request.getStatus().getUser().getAlias());
//        for (String follower : followers) {
//            daoFactory.getFeedDAO().updateFeed(follower, request);
//        }

        return new PostStatusResponse();
    }

    public StoryResponse getStory(StoryRequest request) {
        if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] AuthToken is null");
        } else if (request.getLimit() < 0) {
            throw new RuntimeException("[Bad Request] Limit is less than 0");
        } else if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Alias is null");
        }

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthtoken());
        if (!validAuthtoken) {
            return new StoryResponse("Session expired, please log out and log in again");
        }

        return daoFactory.getStoryDAO().getStory(request);
    }

    public FeedResponse getFeed(FeedRequest request) {
        if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] AuthToken is null");
        } else if (request.getLimit() < 0) {
            throw new RuntimeException("[Bad Request] Limit is less than 0");
        } else if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Alias is null");
        }

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthtoken());
        if (!validAuthtoken) {
            return new FeedResponse("Session expired, please log out and log in again");
        }

        return daoFactory.getFeedDAO().getFeed(request);
    }
}

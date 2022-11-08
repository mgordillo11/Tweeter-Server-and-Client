package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService {
    private DAOFactory daoFactory;

    public StatusService(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        // Eventually, this will post the status to the database.
        if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Status is null");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Ba Request] AuthToken is null");
        } else if (request.getStatus().post == null) {
            throw new RuntimeException("[Bad Request] Status text is null");
        }

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthToken());
        if (!validAuthtoken) {
            return new PostStatusResponse("Authtoken is invalid, and User is no longer active");
        }

        boolean statusPosted = daoFactory.getStoryDAO().postStatus(request.getStatus());
        if (!statusPosted) {
            return new PostStatusResponse("An error occurred while posting the status");
        }

        // Finally, return a response indicating that the status was posted. The correct logic
        // COME BACK TO THIS

        return new PostStatusResponse();
    }

    public StoryResponse getStory(StoryRequest request) {
        if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] AuthToken is null");
        } else if (request.getLimit() < 0) {
            throw new RuntimeException("[Bad Request] Limit is less than 0");
        } else if (request.getLastStatus() == null) {
            throw new RuntimeException("[Bad Request] LastStatus is null");
        } else if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Alias is null");
        }

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthtoken());
        if (!validAuthtoken) {
            return new StoryResponse("Authtoken is invalid, and User is no longer active");
        }

        return daoFactory.getStoryDAO().getStory(request);

//        // Eventually, this will get the story from the database.
//        Pair<List<Status>, Boolean> storyInfo = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
//        return new StoryResponse(storyInfo.getFirst(), storyInfo.getSecond());
    }

    public FeedResponse getFeed(FeedRequest request) {
        if (request.getAuthtoken() == null) {
            throw new RuntimeException("[Bad Request] AuthToken is null");
        } else if (request.getLimit() < 0) {
            throw new RuntimeException("[Bad Request] Limit is less than 0");
        } else if (request.getLastStatus() == null) {
            throw new RuntimeException("[Bad Request] LastStatus is null");
        } else if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Alias is null");
        }

        boolean validAuthtoken = daoFactory.getAuthtokenDAO().isValidAuthToken(request.getAuthtoken());
        if (!validAuthtoken) {
            return new FeedResponse("Authtoken is invalid, and User is no longer active");
        }

        return daoFactory.getFeedDAO().getFeed(request);

//        // Eventually, this will get the feed from the database.
//        Pair<List<Status>, Boolean> feedInfo = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
//        return new FeedResponse(feedInfo.getFirst(), feedInfo.getSecond());
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}

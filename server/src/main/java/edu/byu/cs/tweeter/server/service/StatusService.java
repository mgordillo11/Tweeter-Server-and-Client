package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService {

    public PostStatusResponse postStatus(PostStatusRequest request) {
        // Eventually, this will post the status to the database.
        if (request.getStatus() == null) {
            throw new RuntimeException("[BadRequest] Status is null");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[BadRequest] AuthToken is null");
        } else if (request.getStatus().post == null) {
            throw new RuntimeException("[BadRequest] Status text is null");
        }

        return new PostStatusResponse();
    }

    public StoryResponse getStory(StoryRequest request) {
        if (request.getAuthtoken() == null) {
            throw new RuntimeException("[BadRequest] AuthToken is null");
        } else if (request.getLimit() < 0) {
            throw new RuntimeException("[BadRequest] Limit is less than 0");
        } else if (request.getLastStatus() == null) {
            throw new RuntimeException("[BadRequest] LastStatus is null");
        } else if (request.getAlias() == null) {
            throw new RuntimeException("[BadRequest] Alias is null");
        }

        // Eventually, this will get the story from the database.
        Pair<List<Status>, Boolean> storyInfo = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());

        return new StoryResponse(storyInfo.getFirst(), storyInfo.getSecond());
    }

    public FeedResponse getFeed(FeedRequest request) {
        if (request.getAuthtoken() == null) {
            throw new RuntimeException("[BadRequest400] AuthToken is null");
        } else if (request.getLimit() < 0) {
            throw new RuntimeException("[BadRequest400] Limit is less than 0");
        } else if (request.getLastStatus() == null) {
            throw new RuntimeException("[BadRequest400] LastStatus is null");
        } else if (request.getAlias() == null) {
            throw new RuntimeException("[BadRequest400] Alias is null");
        }

        // Eventually, this will get the feed from the database.
        Pair<List<Status>, Boolean> feedInfo = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());

        return new FeedResponse(feedInfo.getFirst(), feedInfo.getSecond());
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

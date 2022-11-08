package edu.byu.cs.tweeter.server.dao.DAOInterfaces;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

public interface IStoryDAO {
    StoryResponse getStory(StoryRequest request);

    boolean postStatus(Status status);
}

package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDB.DynamoDBFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetStoryHandler implements RequestHandler <StoryRequest, StoryResponse> {

    @Override
    public StoryResponse handleRequest(StoryRequest input, Context context) {
        StatusService statusService = new StatusService(new DynamoDBFactory());
        return statusService.getStory(input);
    }
}

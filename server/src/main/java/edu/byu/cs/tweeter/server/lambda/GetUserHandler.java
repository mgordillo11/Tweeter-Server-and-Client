package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDB.DynamoDBFactory;
import edu.byu.cs.tweeter.server.service.UserService;

public class GetUserHandler implements RequestHandler<GetUserRequest, GetUserResponse> {

    @Override
    public GetUserResponse handleRequest(GetUserRequest input, Context context) {
        UserService userService = new UserService(new DynamoDBFactory());
        return userService.getUser(input);
    }
}


package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDB.DynamoDBFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class FollowHandler implements RequestHandler<FollowRequest, FollowResponse> {
    public static void main(String[] args) {
        Authtoken authtoken = new Authtoken("ea5c553c-ab43-4374-88c9-52de5a283f05", "2022-11-11");
        User user = new User("Carlos", "Valdez", "@carlos", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        FollowRequest request = new FollowRequest(authtoken, user);

        FollowService followService = new FollowService(new DynamoDBFactory());
        followService.follow(request);
    }

    @Override
    public FollowResponse handleRequest(FollowRequest request, Context context) {
        FollowService followService = new FollowService(new DynamoDBFactory());
        return followService.follow(request);
    }
}

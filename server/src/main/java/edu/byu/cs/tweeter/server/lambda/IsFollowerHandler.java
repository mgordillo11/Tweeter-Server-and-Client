package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDB.DynamoDBFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class IsFollowerHandler implements RequestHandler<IsFollowerRequest, IsFollowerResponse> {
    public static void main(String[] args) {
        Authtoken authtoken = new Authtoken("369ce346-37b8-44a1-9593-706d218b561e", "2022-11-14");
        User manny = new User("Manny", "Manny", "@ryth", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User carlos = new User("Carlos", "Carlos", "@carlos", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        IsFollowerRequest request = new IsFollowerRequest(authtoken, manny, carlos);

        FollowService followService = new FollowService(new DynamoDBFactory());
        IsFollowerResponse test = followService.isFollower(request);
        System.out.println(test);
    }

    @Override
    public IsFollowerResponse handleRequest(IsFollowerRequest input, Context context) {
        FollowService followService = new FollowService(new DynamoDBFactory());
        return followService.isFollower(input);
    }
}

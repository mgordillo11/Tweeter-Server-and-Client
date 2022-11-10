package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.time.LocalDate;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDB.DynamoDBFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowingCountHandler implements RequestHandler<GetFollowingCountRequest, GetFollowingCountResponse> {
    public static void main(String[] args) {
        Authtoken authtoken = new Authtoken("48650a11-931d-4709-9a54-c765a83d7a06", LocalDate.now().toString());
        User user = new User("FirstName", "LastName", "@ryth", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        GetFollowingCountRequest request = new GetFollowingCountRequest(authtoken, user);


        FollowService followService = new FollowService(new DynamoDBFactory());
        followService.getFollowingCount(request);
    }


    @Override
    public GetFollowingCountResponse handleRequest(GetFollowingCountRequest input, Context context) {
        FollowService followService = new FollowService(new DynamoDBFactory());
        return followService.getFollowingCount(input);
    }
}

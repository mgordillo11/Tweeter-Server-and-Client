package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDB.DynamoDBFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersHandler implements RequestHandler<FollowersRequest, FollowersResponse> {
    public static void main(String[] args) {
        Authtoken authtoken = new Authtoken("6e4a188c-9e07-463e-86f5-d2edd9a7409a", "2022-11-14");
        //User user = new User("Carlos", "Valdez", "@ryth", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        FollowersRequest request = new FollowersRequest(authtoken, "@ryth", 10, null);
        FollowService service = new FollowService(new DynamoDBFactory());
        service.getFollowers(request);
    }

    @Override
    public FollowersResponse handleRequest(FollowersRequest request, Context context) {
        FollowService service = new FollowService(new DynamoDBFactory());
        return service.getFollowers(request);
    }
}

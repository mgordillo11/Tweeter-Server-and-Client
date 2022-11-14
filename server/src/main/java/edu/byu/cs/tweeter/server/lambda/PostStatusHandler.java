package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.sql.Timestamp;
import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.Authtoken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDB.DynamoDBFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class PostStatusHandler implements RequestHandler<PostStatusRequest, PostStatusResponse> {
    public static void main(String[] args) {

        Authtoken authtoken = new Authtoken("1f5200af-4161-46ae-ac60-283c565ba4f2", "2022-11-11");


        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String timeStamp = String.valueOf(ts.getTime());


        User user = new User("Carlos", "Valdez", "@carlos", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        Status status = new Status("This is a test status", user, timeStamp, new ArrayList<>(), new ArrayList<>());


        PostStatusRequest postStatusRequest = new PostStatusRequest(authtoken, status);

        StatusService statusService = new StatusService(new DynamoDBFactory());
        statusService.postStatus(postStatusRequest);
    }

    @Override
    public PostStatusResponse handleRequest(PostStatusRequest input, Context context) {
        StatusService service = new StatusService(new DynamoDBFactory());
        return service.postStatus(input);
    }
}

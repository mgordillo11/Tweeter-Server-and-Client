package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.server.service.UserService;

public class GetUserHandler implements RequestHandler<Map<String, String>, GetUserResponse> {
    @Override
    public GetUserResponse handleRequest(Map<String, String> input, Context context) {
//        String authtoken = input.get("Authorization");
//        String alias = input.get("Alias");

        String authtoken = "hello";
        String alias = "@bob";

        UserService service = new UserService();
        return service.getUser(alias, authtoken);
    }
}


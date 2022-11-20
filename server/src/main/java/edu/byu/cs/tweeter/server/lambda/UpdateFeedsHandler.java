package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.DynamoDB.DynamoDBFactory;

public class UpdateFeedsHandler implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        Gson gson = new Gson();
        DAOFactory factory = new DynamoDBFactory();

        for (SQSEvent.SQSMessage message : sqsEvent.getRecords()) {
            // Print out the body of the message
            System.out.println("Message Body: " + message.getBody());

            String messageBody = gson.toJson(message.getBody());
            JsonObject messageBodyObject = gson.fromJson(messageBody, JsonObject.class);

            Status postedStatus = gson.fromJson(messageBodyObject.get("Status"), Status.class);
            JsonArray followers = messageBodyObject.getAsJsonArray("Followers");

            List<String> followerAliases = new ArrayList<>();

            followers.forEach(follower -> followerAliases.add(follower.getAsString()));

            factory.getFeedDAO().addFeedBatch(postedStatus, followerAliases);
        }
        return null;
    }
}

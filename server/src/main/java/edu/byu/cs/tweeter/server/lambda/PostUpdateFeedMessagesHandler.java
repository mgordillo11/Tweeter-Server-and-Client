package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.DynamoDB.DynamoDBFactory;

public class PostUpdateFeedMessagesHandler implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        Gson gson = new GsonBuilder().create();
        DAOFactory factory = new DynamoDBFactory();

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        String queueUrl = "https://sqs.us-east-1.amazonaws.com/669525525844/UpdateFeeds";

        final int maxMessagesPerBatch = 100;

        for (SQSEvent.SQSMessage message : sqsEvent.getRecords()) {
            // Print out the body of the message
            System.out.println(message.getBody());

            // Get the status from the message
            Status status = gson.fromJson(message.getBody(), Status.class);

            System.out.println("Status: " + status);

            // Get the list of followers
            List<String> followerAliases = factory.getFollowDAO().getFollowersAlias(status.getUser().getAlias());
            System.out.println("Followers: " + Arrays.toString(followerAliases.toArray()));

            //JsonElement statusJsonElement = gson.toJsonTree(status, Status.class);

            int currentIndexOfFollowers = 0;
            boolean isLastMessage = false;

            while (!isLastMessage) {
                JsonObject messageBodyObject = new JsonObject();
                JsonArray jsonFollowers = new JsonArray();

                int currentMessageCount = 0;
                for (int i = currentIndexOfFollowers; i < followerAliases.size() && currentMessageCount < maxMessagesPerBatch; i++) {
                    jsonFollowers.add(followerAliases.get(i));

                    if (i == followerAliases.size() - 1) {
                        isLastMessage = true;
                        break;
                    }

                    currentIndexOfFollowers++;
                    currentMessageCount++;
                }

                // Add the original status to the message body, this it's already in JSON format
                messageBodyObject.addProperty("status", message.getBody());
                messageBodyObject.add("followers", jsonFollowers);

                String messageBody = gson.toJson(messageBodyObject, JsonObject.class);

                System.out.println("Message Body: " + messageBody);

                try {
                    JsonObject messageAttributes = gson.fromJson(message.getBody(), JsonObject.class);
                    System.out.println("Message Attributes: " + messageAttributes);
                } catch (Exception e) {
                    System.out.println("Error parsing message attributes: " + e.getMessage());
                }

                SendMessageRequest sendMessageRequest = new SendMessageRequest()
                        .withQueueUrl(queueUrl)
                        .withMessageBody(messageBody);

                SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);

                System.out.println("Message Result ID: " + sendMessageResult.getMessageId());
            }
        }
        return null;
    }
}

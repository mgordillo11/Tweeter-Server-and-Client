package edu.byu.cs.tweeter.server.dao.DynamoDB;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public abstract class DynamoDBMainDAO {
    private static final Region region = Region.US_EAST_1;

    private static final DynamoDbClient ddb = DynamoDbClient.builder()
            .region(region).build();

    // DynamoDB enhanced client
    protected final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient
            .builder().dynamoDbClient(ddb).build();

}
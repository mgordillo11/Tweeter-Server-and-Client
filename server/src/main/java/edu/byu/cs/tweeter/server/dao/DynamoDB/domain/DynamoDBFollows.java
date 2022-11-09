package edu.byu.cs.tweeter.server.dao.DynamoDB.domain;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class DynamoDBFollows {
    private String follower_handle;
    private String followee_handle;

    public DynamoDBFollows() {
    }

    public DynamoDBFollows(String follower_handle, String followee_handle) {
        this.follower_handle = follower_handle;
        this.followee_handle = followee_handle;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = "follows_index")
    public String getFollower_handle() {
        return follower_handle;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = "follows_index")
    public String getFollowee_handle() {
        return followee_handle;
    }


    public void setFollower_handle(String follower_handle) {
        this.follower_handle = follower_handle;
    }

    public void setFollowee_handle(String followee_handle) {
        this.followee_handle = followee_handle;
    }
}

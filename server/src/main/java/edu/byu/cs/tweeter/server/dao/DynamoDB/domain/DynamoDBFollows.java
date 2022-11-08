package edu.byu.cs.tweeter.server.dao.DynamoDB.domain;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class DynamoDBFollows {
    private String follower_handle;
    private String follower_name;
    private String followee_handle;
    private String followee_name;

    public DynamoDBFollows() {
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = "follows_index")
    public String getFollower_handle() {
        return follower_handle;
    }

    public String getFollower_name() {
        return follower_name;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = "follows_index")
    public String getFollowee_handle() {
        return followee_handle;
    }

    public String getFollowee_name() {
        return followee_name;
    }

    public void setFollower_handle(String follower_handle) {
        this.follower_handle = follower_handle;
    }

    public void setFollower_name(String follower_name) {
        this.follower_name = follower_name;
    }

    public void setFollowee_handle(String followee_handle) {
        this.followee_handle = followee_handle;
    }

    public void setFollowee_name(String followee_name) {
        this.followee_name = followee_name;
    }
}

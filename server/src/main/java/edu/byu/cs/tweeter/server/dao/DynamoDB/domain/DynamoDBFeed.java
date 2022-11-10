package edu.byu.cs.tweeter.server.dao.DynamoDB.domain;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class DynamoDBFeed {
    public String receiver_alias;
    public Long datetime;

    public DynamoDBStatus status;

    public DynamoDBFeed() {
    }

    public DynamoDBFeed(String receiver_alias, Long datetime, DynamoDBStatus status) {
        this.receiver_alias = receiver_alias;
        this.datetime = datetime;
        this.status = status;
    }

    @DynamoDbPartitionKey
    public String getReceiver_alias() {
        return receiver_alias;
    }

    public void setReceiver_alias(String receiver_alias) {
        this.receiver_alias = receiver_alias;
    }

    @DynamoDbSortKey
    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    public DynamoDBStatus getStatus() {
        return status;
    }

    public void setStatus(DynamoDBStatus status) {
        this.status = status;
    }
}

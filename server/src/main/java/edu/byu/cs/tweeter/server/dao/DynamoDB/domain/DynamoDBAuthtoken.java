package edu.byu.cs.tweeter.server.dao.DynamoDB.domain;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class DynamoDBAuthtoken {
    private String token;
    private String timeStamp;
    private String alias;

    public DynamoDBAuthtoken() {
    }

    public DynamoDBAuthtoken(String token, String timeStamp, String alias) {
        this.token = token;
        this.timeStamp = timeStamp;
        this.alias = alias;
    }

    @DynamoDbPartitionKey
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}

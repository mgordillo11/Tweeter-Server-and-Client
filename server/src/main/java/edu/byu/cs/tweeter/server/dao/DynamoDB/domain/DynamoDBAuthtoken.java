package edu.byu.cs.tweeter.server.dao.DynamoDB.domain;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class DynamoDBAuthtoken {
    /**
     * Value of the auth token.
     */
    public String token;

    public String alias;

    /**
     * String representation of date/time at which the auth token was created.
     */
    public String datetime;

    public DynamoDBAuthtoken() {
    }

    public DynamoDBAuthtoken(String token) {
        this.token = token;
    }

    public DynamoDBAuthtoken(String token, String datetime, String alias) {
        this.token = token;
        this.datetime = datetime;
        this.alias = alias;
    }


    @DynamoDbPartitionKey
    public String getToken() {
        return token;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDatetime() {
        return datetime;
    }
}

package edu.byu.cs.tweeter.server.dao.DynamoDB.domain;

import java.util.List;
import java.util.Objects;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class DynamoDBStatus {
    /**
     * Text for the status.
     */
    public String post;
    /**
     * User who sent the status.
     */
    public String userAlias;
    /**
     * String representation of the date/time at which the status was sent.
     */
    public Long datetime;
    /**
     * URLs contained in the post text.
     */
    public List<String> urls;
    /**
     * User mentions contained in the post text.
     */
    public List<String> mentions;

    public DynamoDBStatus() {
    }

    public DynamoDBStatus(String post, String userAlias, Long datetime, List<String> urls, List<String> mentions) {
        this.post = post;
        this.userAlias = userAlias;
        this.datetime = datetime;
        this.urls = urls;
        this.mentions = mentions;
    }

    @DynamoDbPartitionKey
    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    @DynamoDbSortKey
    public Long getDate() {
        return datetime;
    }

    public String getPost() {
        return post;
    }

    public List<String> getUrls() {
        return urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamoDBStatus status = (DynamoDBStatus) o;
        return Objects.equals(post, status.post) &&
                Objects.equals(userAlias, status.userAlias) &&
                Objects.equals(datetime, status.datetime) &&
                Objects.equals(mentions, status.mentions) &&
                Objects.equals(urls, status.urls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, userAlias, datetime, mentions, urls);
    }

    @Override
    public String toString() {
        return "Status{" +
                "post='" + post + '\'' +
                ", user=" + userAlias +
                ", datetime=" + datetime +
                ", mentions=" + mentions +
                ", urls=" + urls +
                '}';
    }
}

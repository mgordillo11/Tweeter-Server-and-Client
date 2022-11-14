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
     * User who is posting the status.
     */
    public String sender_alias;
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

    public DynamoDBStatus(String post, String sender_alias, Long datetime, List<String> urls, List<String> mentions) {
        this.post = post;
        this.sender_alias = sender_alias;
        this.datetime = datetime;
        this.urls = urls;
        this.mentions = mentions;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @DynamoDbPartitionKey
    public String getSender_alias() {
        return sender_alias;
    }

    public void setSender_alias(String sender_alias) {
        this.sender_alias = sender_alias;
    }

    @DynamoDbSortKey
    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamoDBStatus status = (DynamoDBStatus) o;
        return Objects.equals(post, status.post) &&
                Objects.equals(sender_alias, status.sender_alias) &&
                Objects.equals(datetime, status.datetime) &&
                Objects.equals(mentions, status.mentions) &&
                Objects.equals(urls, status.urls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, sender_alias, datetime, mentions, urls);
    }

    @Override
    public String toString() {
        return "Status{" +
                "post='" + post + '\'' +
                ", user=" + sender_alias +
                ", datetime=" + datetime +
                ", mentions=" + mentions +
                ", urls=" + urls +
                '}';
    }
}

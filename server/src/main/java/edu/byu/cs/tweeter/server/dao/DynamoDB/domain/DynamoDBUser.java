package edu.byu.cs.tweeter.server.dao.DynamoDB.domain;

import java.util.Objects;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class DynamoDBUser implements Comparable<DynamoDBUser> {
    private String firstName;
    private String lastName;
    private String alias;
    private String password;
    private String imageUrl;

    public DynamoDBUser() {
    }

    public DynamoDBUser(String firstName, String lastName, String password, String imageURL) {
        this(firstName, lastName, String.format("@%s%s", firstName, lastName), password, imageURL);
    }

    public DynamoDBUser(String firstName, String lastName, String alias, String password, String imageURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.imageUrl = imageURL;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return String.format("%s %s", firstName, lastName);
    }

    @DynamoDbPartitionKey
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamoDBUser user = (DynamoDBUser) o;
        return alias.equals(user.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", alias='" + alias + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public int compareTo(DynamoDBUser user) {
        return this.getAlias().compareTo(user.getAlias());
    }
}

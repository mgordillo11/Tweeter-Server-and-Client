package edu.byu.cs.tweeter.server.dao.DynamoDB;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DAOInterfaces.IUserDAO;
import edu.byu.cs.tweeter.server.dao.DynamoDB.domain.DynamoDBUser;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;

public class DynamoDBUserDAO extends DynamoDBMainDAO implements IUserDAO {
    private static final String TABLE_NAME = "users";
    private final DynamoDbTable<DynamoDBUser> table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(DynamoDBUser.class));

    @Override
    public User login(String username) {
        try {
            return getUser(username);
        } catch (Exception e) {
            System.err.println("Unable to get user");
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void register(String username, String password, String firstName, String lastName, String imageURL) {
        DynamoDBUser user = new DynamoDBUser(firstName, lastName, username, password, imageURL);
        try {
            table.putItem(user);
        } catch (Exception e) {
            System.err.println("Unable to add user");
            System.err.println(e.getMessage());
        }
    }

    @Override
    public User getUser(String username) {
        DynamoDBUser user = getCompleteUser(username);
        if (user == null) {
            return null;
        }

        return new User(user.getFirstName(), user.getLastName(), user.getAlias(), user.getImageUrl());
    }

    @Override
    public String getHashedPassword(String username) {
        return getCompleteUser(username).getPassword();
    }

    public DynamoDBUser getCompleteUser(String username) {
        Key key = Key.builder().partitionValue(username).build();

        return table.getItem(
                (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));
    }
}
